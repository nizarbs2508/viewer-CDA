package crypto;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoExample {

  public static void main(String[] args) throws Exception {

    /* Utilisation de la classe util */

    AESFileCryptoUtil aesCryptoUtil = new AESFileCryptoUtil();

    /* Chiffre le fichier "fichier.txt" vers le "fichier.crypt" */
    aesCryptoUtil.cryptOrDecryptFile(Cipher.ENCRYPT_MODE, "password", new File("fichier.txt"),
      new File("fichier.crypt"));

    /* Déchiffre le fichier "fichier.crypt" vers le "fichier-decrypt.txt" */
    aesCryptoUtil.cryptOrDecryptFile(Cipher.DECRYPT_MODE, "password", new File("fichier.crypt"),
      new File("fichier-decrypt.txt"));
  }

}

class AESFileCryptoUtil {

  /**
   * Permet de chiffrer ou déchiffrer un fichier
   * 
   * @param mode     chiffrer (1) ou déchiffrer (2)
   * @param password Le mot de passe
   * @param input    Le fichier à chiffrer ou à déchiffrer
   * @param output   Le fichier chiffrer ou déchiffrer
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws IOException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws Exception
   * 
   * @see Cipher.ENCRYPT_MODE
   * @see Cipher.DECRYPT_MODE
   * 
   */
  public void cryptOrDecryptFile(int mode, String password, File input, File output) throws NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

      SecretKey secret = generateSecretKeyWithPassword(password);

      /* Utilisation de l'algorithme AES */
      Cipher aesCipher = Cipher.getInstance("AES");
      aesCipher.init(mode, secret);

      /* Lecture du contenu au format byte */
      byte[] content = Files.readAllBytes(input.toPath());

      /* Chiffre ou déchiffre le contenu du fichier */
      byte[] resultContent = aesCipher.doFinal(content);

      /* Création et écriture du contenu dans le fichier de sortie */
      Files.write(output.toPath(), resultContent, StandardOpenOption.CREATE);
    }

  /**
   * Génère une clé secrète avec le mot de passe
   * 
   * @param password Le mot de passe à transformer en clé de chiffrement
   * @return La clé générée à partir du mot de passe
   * @throws NoSuchAlgorithmException
   * @throws UnsupportedEncodingException
   */
  private SecretKey generateSecretKeyWithPassword(String password)
  throws NoSuchAlgorithmException, UnsupportedEncodingException {

    /* une clé de 256 bits pour l'algorithme AES-256 */
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    /*
     * On prend les bytes du fichier. Le charset UTF-8 est important en cas
     * d'accentuation des caractères dans le mot de passe
     */
    byte[] key = sha.digest(password.getBytes("UTF-8"));
    SecretKey secret = new SecretKeySpec(key, "AES");
    return secret;
  }

}
