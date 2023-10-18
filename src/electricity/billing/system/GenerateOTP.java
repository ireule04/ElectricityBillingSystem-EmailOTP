package electricity.billing.system;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.CharacterPredicates;

public class GenerateOTP {

    // Generate a random 6-digit OTP
    public static String generateOTP() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .filteredBy(CharacterPredicates.DIGITS)
                .build();

        return generator.generate(6);
    }

    public static void main(String[] args) {
        // Example usage:
        String otp = generateOTP();
        System.out.println("Generated OTP: " + otp);
    }
}
