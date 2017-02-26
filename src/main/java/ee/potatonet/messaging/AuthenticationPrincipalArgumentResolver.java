package ee.potatonet.messaging;

import java.lang.annotation.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return findMethodAnnotation(AuthenticationPrincipal.class, parameter) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
    /* Copied from spring's PrincipalMethodArgumentResolver */
    Authentication authentication = (Authentication) SimpMessageHeaderAccessor.getUser(message.getHeaders());

    Object principal = authentication.getPrincipal();

    /* Copied from spring's AuthenticationPrincipalArgumentResolver */
    if (principal != null
        && !parameter.getParameterType().isAssignableFrom(principal.getClass())) {
      AuthenticationPrincipal authPrincipal = findMethodAnnotation(
          AuthenticationPrincipal.class, parameter);
      if (authPrincipal.errorOnInvalidType()) {
        throw new ClassCastException(principal + " is not assignable to "
            + parameter.getParameterType());
      }
      else {
        return null;
      }
    }
    return principal;
  }

  /* Copied from spring's AuthenticationPrincipalArgumentResolver */
  /**
   * Obtains the specified {@link Annotation} on the specified {@link MethodParameter}.
   *
   * @param annotationClass the class of the {@link Annotation} to find on the
   * {@link MethodParameter}
   * @param parameter the {@link MethodParameter} to search for an {@link Annotation}
   * @return the {@link Annotation} that was found or null.
   */
  private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
                                                        MethodParameter parameter) {
    T annotation = parameter.getParameterAnnotation(annotationClass);
    if (annotation != null) {
      return annotation;
    }
    Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
    for (Annotation toSearch : annotationsToSearch) {
      annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(),
          annotationClass);
      if (annotation != null) {
        return annotation;
      }
    }
    return null;
  }
}
