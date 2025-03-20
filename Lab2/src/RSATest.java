import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class RSATest {
    public static void main(String[] args) throws IOException {
        long p = RSAUtil.findLargePrime();
        long q = RSAUtil.findLargePrime();
        long n = p * q;
        long faiN = (p - 1) * (q - 1);
        long e = RSAUtil.findE(faiN);
        long d = RSAUtil.extendEuclid(faiN, e);

        BufferedReader reader = new BufferedReader(new FileReader("lab2-Plaintext.txt"));
        String plainText = reader.readLine();
        reader.close();

        List<Character> newPlainText = RSAUtil.plaintextReprocess(plainText);
        List<Integer> plainTextNum = RSAUtil.text2num(newPlainText);

        // 开始计时
        long startTime = System.nanoTime();
        List<Long> cipherTextNum = RSAUtil.encryption(plainTextNum, e, n);
        // 结束计时
        long endTime = System.nanoTime();

        long encryptionTimeNano = endTime - startTime;
        System.out.println("加密时长：" + encryptionTimeNano + " 纳秒");


        List<Integer> decPlainTextNum = RSAUtil.decryption(cipherTextNum, d, n);
        List<Character> decPlainText = RSAUtil.num2text(decPlainTextNum);

        StringBuilder newText = new StringBuilder();
        for (char ch : newPlainText) {
            newText.append(ch);
        }

        StringBuilder decryptedText = new StringBuilder();
        for (char ch : decPlainText) {
            decryptedText.append(ch);
        }

        assert newText.toString().equals(decryptedText.toString());

        System.out.println("p = " + p + "\nq = " + q + "\nn = " + n + "\ne = " + e + "\nd = " + d + "\nφ(n) = " + faiN);
        System.out.println("加密后的密文为：" + cipherTextNum);
        System.out.println("解密后的明文为：" + decryptedText);
    }
}
