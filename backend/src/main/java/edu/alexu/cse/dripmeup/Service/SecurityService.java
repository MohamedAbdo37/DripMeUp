package edu.alexu.cse.dripmeup.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import edu.alexu.cse.dripmeup.CustomAuthentication;

public class SecurityService {

    public static Long getIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof CustomAuthentication) {
            CustomAuthentication customAuth = (CustomAuthentication) authentication;
            return customAuth.getId();
        }
        return null;
    }
}