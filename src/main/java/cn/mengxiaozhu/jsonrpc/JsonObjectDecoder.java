package cn.mengxiaozhu.jsonrpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class JsonObjectDecoder extends ByteToMessageDecoder {

    private static final int ST_CORRUPTED = -1;
    private static final int ST_INIT = 0;
    private static final int ST_DECODING_NORMAL = 1;

    private int openBraces;
    private int idx;

    private int lastReaderIndex;

    private int state;
    private boolean insideString;

    private final int maxObjectLength;


    /**
     * @param maxObjectLength     maximum number of bytes a JSON object/array may use (including braces and all).
     *                            Objects exceeding this length are dropped and an {@link TooLongFrameException}
     *                            is thrown.
     */
    public JsonObjectDecoder(int maxObjectLength) {
        if (maxObjectLength < 1) {
            throw new IllegalArgumentException("maxObjectLength must be a positive int");
        }
        this.maxObjectLength = maxObjectLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (state == ST_CORRUPTED) {
            in.skipBytes(in.readableBytes());
            return;
        }

        if (this.idx > in.readerIndex() && lastReaderIndex != in.readerIndex()) {
            this.idx = in.readerIndex();

            state = 0;
            insideString = false;
        }


        // index of next byte to process.
        int idx = this.idx;
        int wrtIdx = in.writerIndex();

        if (wrtIdx > maxObjectLength) {
            // buffer size exceeded maxObjectLength; discarding the complete buffer.
            in.skipBytes(in.readableBytes());
            reset();
            throw new TooLongFrameException(
                    "object length exceeds " + maxObjectLength + ": " + wrtIdx + " bytes discarded");
        }

        for (/* use current idx */; idx < wrtIdx; idx++) {

            byte c = in.getByte(idx);

            if (state == ST_DECODING_NORMAL) {
                decodeByte(c, in, idx);

                // All opening braces/brackets have been closed. That's enough to conclude
                // that the JSON object/array is complete.
                if (openBraces == 0) {
                    ByteBuf json = extractObject(ctx, in, in.readerIndex(), idx + 1 - in.readerIndex());
                    if (json != null) {
                        out.add(json);
                    }

                    // The JSON object/array was extracted => discard the bytes from
                    // the input buffer.
                    in.readerIndex(idx + 1);
                    // Reset the object state to get ready for the next JSON object/text
                    // coming along the byte stream.
                    reset();
                }
            } else if (c == '{' || c == '[') {
                initDecoding(c);
                // Discard leading spaces in front of a JSON object/array.
            } else if (Character.isWhitespace(c)) {
                in.skipBytes(1);
            } else {
                state = ST_CORRUPTED;
                throw new CorruptedFrameException(
                        "invalid JSON received at byte position " + idx + ": " + ByteBufUtil.hexDump(in));
            }
        }

        if (in.readableBytes() == 0) {
            this.idx = 0;
        } else {
            this.idx = idx;
        }
        this.lastReaderIndex = in.readerIndex();
    }

    /**
     * Override this method if you want to filter the json objects/arrays that get passed through the pipeline.
     */
    @SuppressWarnings("UnusedParameters")
    private ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        return buffer.retainedSlice(index, length);
    }

    private void decodeByte(byte c, ByteBuf in, int idx) {
        if ((c == '{' || c == '[') && !insideString) {

            openBraces++;
        } else if ((c == '}' || c == ']') && !insideString) {

            openBraces--;
        } else if (c == '"') {
            // start of a new JSON string. It's necessary to detect strings as they may
            // also contain braces/brackets and that could lead to incorrect results.
            if (!insideString) {
                insideString = true;
            } else {
                int backslashCount = 0;
                idx--;
                while (idx >= 0) {
                    if (in.getByte(idx) == '\\') {
                        backslashCount++;
                        idx--;
                    } else {
                        break;
                    }
                }
                // The double quote isn't escaped only if there are even "\"s.
                if (backslashCount % 2 == 0) {
                    // Since the double quote isn't escaped then this is the end of a string.
                    insideString = false;
                }
            }
        }
    }

    private void initDecoding(byte openingBrace) {
        openBraces = 1;
        state = ST_DECODING_NORMAL;
    }

    private void reset() {
        insideString = false;
        state = ST_INIT;
        openBraces = 0;
    }
}

