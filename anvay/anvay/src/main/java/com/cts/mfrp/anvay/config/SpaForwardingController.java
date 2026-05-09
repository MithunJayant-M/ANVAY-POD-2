package com.cts.mfrp.anvay.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards every non-API, non-asset request to /index.html so that Angular's
 * client-side router can handle deep links and page refreshes when the
 * frontend is bundled into Spring Boot's static resources (single-service
 * Docker deploy).
 *
 * The {@code [^\.]*} regex matches path segments that do NOT contain a dot,
 * so static files (script.js, styles.css, logo.png) fall through to the
 * default static-resource handler. Spring's controller-mapping precedence
 * means /api/**, /v3/api-docs/**, /swagger-ui/** still hit their own
 * controllers before this catch-all is considered.
 */
@Controller
public class SpaForwardingController {

    @RequestMapping(value = {
        "/{path:[^\\.]*}",         // /login, /dashboard
        "/**/{path:[^\\.]*}"       // /dashboard/student, /dashboard/student/profile
    })
    public String forward() {
        return "forward:/index.html";
    }
}
