package wiki.sogou.jmail.util;

import java.util.Locale;
import java.util.Map;

/**
 * @author JimYip
 */
public class StringUtils {
    public static String replaceVars(String s, Map<?, ?> varMap) {
        int i0 = 0;
        int i1 = s.indexOf('$');
        int i2;

        if (i1 < 0) {
            return s;
        }

        StringBuilder buf = new StringBuilder();
        while ((i2 = s.indexOf('$', i1 + 1)) > 0) {
            String var = s.substring(i1 + 1, i2).toLowerCase(Locale.US);
            Object val = varMap.get(var);
            if (val == null) {
                if (!varMap.containsKey(var)) {
                    i1 = i2;
                    continue;
                }
                val = "";
            }
            buf.append(s, i0, i1);
            buf.append(val);
            i0 = i2 + 1;
            i1 = s.indexOf('$', i0);
            if (i1 < 0) {
                break;
            }
        }

        if (i0 == 0) {
            return s;
        }

        return buf.append(s.substring(i0)).toString();
    }

    public static byte[] iso2Bytes(CharSequence s) {
        if (s instanceof BytesAsCharSequence) {
            BytesAsCharSequence bc = ((BytesAsCharSequence) s);
            if ((bc.offset == 0) && (bc.count == bc.value.length)) {
                return bc.value;
            }
        }

        int len = s.length();
        byte[] dest = new byte[len];

        iso2Bytes(s, 0, dest, 0, len);

        return dest;
    }


    public static void iso2Bytes(CharSequence src, int srcOff, byte[] dest, int destOff, int len) {
        if (src instanceof BytesAsCharSequence) {
            BytesAsCharSequence bc = ((BytesAsCharSequence) src);
            System.arraycopy(bc.value, bc.offset + srcOff, dest, destOff, len);
            return;
        }

        for (int i = 0; i < len; i++) {
            dest[destOff + i] = (byte) (src.charAt(srcOff + i) & 0xff);
        }
    }

    private static class BytesAsCharSequence implements CharSequence {

        private byte[] value;
        private int offset;
        private int count;


        private BytesAsCharSequence(byte[] bytes, int offset, int count) {
            this.value = bytes;
            this.offset = offset;
            this.count = count;
        }

        @Override
        public int length() {
            return count;
        }

        @Override
        public char charAt(int index) {
            if ((index < 0) || (index >= count)) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return (char) (value[index + offset] & 0xff);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            if (start < 0) {
                throw new ArrayIndexOutOfBoundsException(start + " < 0");
            }
            if (end > count) {
                throw new ArrayIndexOutOfBoundsException(end + " > " + count);
            }
            if (end < start) {
                throw new ArrayIndexOutOfBoundsException(end + " < " + start);
            }
            return ((start == 0) && (end == count))
                    ? this : new BytesAsCharSequence(value, offset + start, end - start);
        }


        @Override
        public String toString() {
            return toISOString(value, offset, count);
        }
    }

    public static String toISOString(byte[] a, int offset, int count) {
        StringBuffer dest = new StringBuffer(count);
        int end = offset + count;
        for (; offset < end; offset++) {
            dest.append((char) (a[offset] & 0xff));
        }

        return new String(dest);
    }
}
