package com.railpass.web.authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import io.jsonwebtoken.Jwts;

public class TokenGenerator {
    private final static int ARGON_ITERATIONS = 4;
    private final static int ARGON_MEM_LIMIT = 9216;
    private final static int ARGON_PARALLELISM = 1;
    private final static int HASH_LENGTH = 32;
    private final static int TOKEN_TTL = 2*60*60*1000;

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

    public static KeyPair generateKeyPair(byte[] seed) throws Exception {
        BigInteger d = new BigInteger(1, seed);

        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        BigInteger n = ecSpec.getN();
        d = d.mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);

        ECPoint Q = ecSpec.getG().multiply(d).normalize();

        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
        ECPrivateKeySpec privSpec = new ECPrivateKeySpec(d, ecSpec);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);

        PrivateKey privateKey = keyFactory.generatePrivate(privSpec);
        PublicKey publicKey = keyFactory.generatePublic(pubSpec);
        return new KeyPair(publicKey, privateKey);
    }

    public static String generateToken(String subject, PrivateKey key) {
        Date issuedAt = new Date();
        return Jwts.builder()
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(new Date(issuedAt.getTime() + TOKEN_TTL))
            .signWith(key)
            .compact();
    }
}
