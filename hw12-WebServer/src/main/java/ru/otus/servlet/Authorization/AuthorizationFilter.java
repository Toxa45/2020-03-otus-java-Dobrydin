package ru.otus.servlet.Authorization;


import static ru.otus.servlet.Authorization.TokenConfig.SECRET;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Role;

@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);
  private final List<RoleMatcherURI> roleMatcherURLS;
  private ServletContext context;

  @Override
  public void init(FilterConfig filterConfig) {
    this.context = filterConfig.getServletContext();
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    final String uri = request.getRequestURI();
    this.context.log("Requested Resource:" + uri);

    HttpSession session = request.getSession(false);

    Role role = Role.ROLE_ANONYMOUS;
    if (session != null) {
      String roleToken = (String) session.getAttribute("token");
      if (validateToken(roleToken)) {
        DefaultClaims claims = (DefaultClaims) Jwts.parser()
            .setSigningKey(SECRET)
            .parse(roleToken).getBody();

        if (claims != null) {
          try {
            role = Role.valueOf(Optional.ofNullable((String) claims.get("role"))
                .orElse(Role.ROLE_ANONYMOUS.name())
            );
          } catch (Exception ex) {
            log.error("error parse token to role", ex);
            role = Role.ROLE_ANONYMOUS;
          }
        }
      }
    }

    if (!isAuthorizationAndHasAccess(role, uri)) {
      response.sendRedirect("/login");
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }

  }

  private boolean isAuthorizationAndHasAccess(final Role role, final String uri) {
    return roleMatcherURLS != null &&
        roleMatcherURLS.stream()
            .anyMatch(el -> role.equals(el.getRole()) && Pattern.compile(el.getUri()).matcher(uri).find());
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException expEx) {
      log.warn("Token expired", expEx);
    } catch (UnsupportedJwtException unsEx) {
      log.warn("Unsupported jwt", unsEx);
    } catch (MalformedJwtException mjEx) {
      log.warn("Malformed jwt", mjEx);
    } catch (SignatureException sEx) {
      log.warn("Invalid signature", sEx);
    } catch (Exception e) {
      log.warn("invalid token", e);
    }
    return false;
  }

  @Override
  public void destroy() {

  }

}
