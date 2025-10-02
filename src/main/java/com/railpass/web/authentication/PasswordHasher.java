package com.railpass.web.authentication;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class PasswordHasher {
    private final static int ARGON_ITERATIONS = 4;
    private final static int ARGON_MEM_LIMIT = 9216;
    private final static int ARGON_PARALLELISM = 1;
    private final static int HASH_LENGTH = 32;

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        return salt;
    }

    public static byte[] generateHash(String password, byte[] salt) {
        byte[] result = new byte[HASH_LENGTH];

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(ARGON_ITERATIONS)
            .withMemoryAsKB(ARGON_MEM_LIMIT)
            .withParallelism(ARGON_PARALLELISM)
            .withSalt(salt);
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);

        return result;
    }

}
