package be.intec.themarujohyperblog;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

    public class BCryptTest {
        public static void main(String[] args) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

            String rawPassword = "Mausam!123";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            System.out.println("Raw password: " + rawPassword);
            System.out.println("Encoded password: " + encodedPassword);

            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            System.out.println("Password matches: " + matches);
        }
}
