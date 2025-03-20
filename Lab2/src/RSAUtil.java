import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class RSAUtil {

    // 快速幂算法
    public static long quickPow(long m, long e, long n) {
        long ans = 1;
        while (e != 0) {
            if ((e & 1) == 1) {
                ans = (ans * m) % n;
            }
            m = (m * m) % n;
            e >>= 1;
        }
        return ans;
    }

    // 素性检测
    public static boolean isPrime(long prime) {
        long k = 0;
        long q = prime - 1;
        while (q % 2 == 0) {
            q >>= 1;
            k++;
        }
        Random rand = new Random();
        long a = 2 + rand.nextInt((int) (prime - 2));
        long remain = quickPow(a, q, prime);
        if (remain == 1 || remain == prime - 1) {
            return true;
        }
        for (int i = 1; i < k; i++) {
            if (quickPow(a, (1 << i) * q, prime) == prime - 1) {
                return true;
            }
        }
        return false;
    }

    // 生成大素数
    public static long findLargePrime() {
        Random rand = new Random();
        while (true) {
            long prime = 1001 + rand.nextInt(9000);
            if (prime % 2 == 1 && isPrime(prime)) {
                return prime;
            }
        }
    }

    // 欧几里得算法
    public static long euclid(long a, long b) {
        while (b != 0) {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    // 生成公钥e
    public static long findE(long faiN) {
        Random rand = new Random();
        while (true) {
            long e = 2 + rand.nextInt((int) (faiN - 2));
            if (euclid(e, faiN) == 1) {
                return e;
            }
        }
    }

    // 扩展欧几里得算法
    public static long extendEuclid(long a, long b) {
        long[] x = {1, 0, a};
        long[] y = {0, 1, b};
        long[] t = {0, 0, 0};
        while (y[2] != 1) {
            long q = x[2] / y[2];
            for (int i = 0; i < 3; i++) {
                t[i] = x[i] - q * y[i];
            }
            System.arraycopy(y, 0, x, 0, 3);
            System.arraycopy(t, 0, y, 0, 3);
        }
        return (y[1] + a) % a;
    }

    // 删去非字母数字符号
    public static List<Character> plaintextReprocess(String plainText) {
        List<Character> newText = new ArrayList<>();
        for (char item : plainText.toCharArray()) {
            if (Character.isDigit(item) || Character.isLetter(item)) {
                newText.add(item);
            }
        }
        return newText;
    }

    // 将文本转化为数字
    public static List<Integer> text2num(List<Character> text) {
        List<Integer> textNum = new ArrayList<>();
        for (char item : text) {
            if (Character.isDigit(item)) {
                textNum.add(item - '0');
            } else if (Character.isLowerCase(item)) {
                textNum.add(10 + (item - 'a'));
            } else if (Character.isUpperCase(item)) {
                textNum.add(36 + (item - 'A'));
            }
        }
        return textNum;
    }

    // 将数字转化为文本
    public static List<Character> num2text(List<Integer> textNum) {
        List<Character> text = new ArrayList<>();
        for (int item : textNum) {
            if (0 <= item && item <= 9) {
                text.add((char) ('0' + item));
            } else if (10 <= item && item <= 35) {
                text.add((char) ('a' + item - 10));
            } else if (36 <= item && item <= 61) {
                text.add((char) ('A' + item - 36));
            }
        }
        return text;
    }

    // 加密
    public static List<Long> encryption(List<Integer> plainTextNum, long e, long n) {
        List<Long> cipherTextNum = new ArrayList<>();
        int i = 0;
        while (i < plainTextNum.size()) {
            if (i == plainTextNum.size() - 1) {
                plainTextNum.add(62);
            }
            long plainNum = plainTextNum.get(i) * 100 + plainTextNum.get(i + 1);
            long cipherNum = quickPow(plainNum, e, n);
            cipherTextNum.add(cipherNum);
            i += 2;
        }
        return cipherTextNum;
    }

    // 解密
    public static List<Integer> decryption(List<Long> cipherTextNum, long d, long n) {
        List<Integer> plainTextNum = new ArrayList<>();
        for (long item : cipherTextNum) {
            long plainNum = quickPow(item, d, n);
            plainTextNum.add((int) (plainNum / 100));
            plainTextNum.add((int) (plainNum % 100));
        }
        return plainTextNum;
    }
}