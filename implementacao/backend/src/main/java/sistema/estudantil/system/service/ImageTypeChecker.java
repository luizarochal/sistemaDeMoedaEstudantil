package sistema.estudantil.system.service;

import java.io.IOException;

public class ImageTypeChecker {

    private static final byte[] JPEG_SIGNATURE = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final byte[] WEBP_SIGNATURE = {'R', 'I', 'F', 'F'};

    public static String detectImageType(byte[] data) throws IOException {
        if (data == null || data.length < 8) {
            throw new IOException("Dados de imagem insuficientes para verificação");
        }

        if (startsWith(data, JPEG_SIGNATURE)) {
            return "image/jpeg";
        }

        if (startsWith(data, PNG_SIGNATURE)) {
            return "image/png";
        }

        if (startsWith(data, WEBP_SIGNATURE) && data[8] == 'W' && data[9] == 'E' && data[10] == 'B' && data[11] == 'P') {
            return "image/webp";
        }

        throw new IOException("Tipo de imagem não suportado");
    }

    private static boolean startsWith(byte[] data, byte[] signature) {
        if (data.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if (data[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }
}