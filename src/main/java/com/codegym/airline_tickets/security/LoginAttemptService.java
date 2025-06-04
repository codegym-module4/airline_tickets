package com.codegym.airline_tickets.security;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    // Số lần sai tối đa trước khi khóa
    static final int MAX_FAILED_ATTEMPTS = 3;
    // Thời gian khóa (phút)
    private static final int LOCK_TIME_DURATION = 5;

    // Map lưu số lần thử sai: key = email, value = count
    private final ConcurrentHashMap<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    // Map lưu thời điểm khóa: key = email, value = thời điểm khóa (LocalDateTime)
    private final ConcurrentHashMap<String, LocalDateTime> lockTimeMap = new ConcurrentHashMap<>();

    /**
     * Gọi khi login sai: tăng count, nếu >= MAX_FAILED_ATTEMPTS thì ghi thời điểm khóa.
     */
    public void loginFailed(String email) {
        int attempts = failedAttempts.getOrDefault(email, 0) + 1;
        failedAttempts.put(email, attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            lockTimeMap.put(email, LocalDateTime.now());
        }
    }

    /**
     * Gọi khi login thành công: xóa hết dữ liệu liên quan để reset count và unlock.
     */
    public void loginSucceeded(String email) {
        failedAttempts.remove(email);
        lockTimeMap.remove(email);
    }

    /**
     * Kiểm tra xem email có đang bị khóa hay không.
     * Nếu đã quá LOCK_TIME_DURATION, tự động mở khóa và trả về false.
     */
    public boolean isBlocked(String email) {
        if (!lockTimeMap.containsKey(email)) {
            return false;
        }
        LocalDateTime lockTime = lockTimeMap.get(email);
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = ChronoUnit.MINUTES.between(lockTime, now);

        if (minutesPassed >= LOCK_TIME_DURATION) {
            // Quá 15 phút → unlock
            lockTimeMap.remove(email);
            failedAttempts.remove(email);
            return false;
        }
        return true; // Vẫn đang trong thời gian khóa
    }

    /**
     * Trả về còn bao nhiêu phút phải chờ để mở khóa (nếu đang bị khóa).
     * Nếu không bị khóa, trả về 0.
     */
    public long getRemainingLockTime(String email) {
        if (!lockTimeMap.containsKey(email)) {
            return 0;
        }
        LocalDateTime lockTime = lockTimeMap.get(email);
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = ChronoUnit.MINUTES.between(lockTime, now);
        long remaining = LOCK_TIME_DURATION - minutesPassed;
        return remaining > 0 ? remaining : 0;
    }

    /**
     * Trả về số lần login sai hiện tại của email.
     */
    public int getFailedAttempts(String email) {
        return failedAttempts.getOrDefault(email, 0);
    }
}
