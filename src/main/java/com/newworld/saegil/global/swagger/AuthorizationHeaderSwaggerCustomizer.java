package com.newworld.saegil.global.swagger;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AuthorizationHeaderSwaggerCustomizer implements OperationCustomizer {

    public static final String AUTHORIZATION_HEADER_CUSTOM_PARAMETER_NAME = "Authorization-Header";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ignoreSwaggerParameterAboutAuthorizationHeader(operation);

        if (isNeedAuthorizationHeader(handlerMethod) || hasSecurityRequirement(operation)) {
            operation.addParametersItem(customAuthHeaderParameter());
            operation.addSecurityItem(
                    new SecurityRequirement().addList(SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
            );
        }

        return operation;
    }

    private void ignoreSwaggerParameterAboutAuthorizationHeader(final Operation operation) {
        if (operation.getParameters() == null) {
            return;
        }

        operation.setParameters(
                operation.getParameters().stream()
                         .filter(this::hasNothingToDoWithAuthorizationHeader)
                         .collect(Collectors.toList()));

    }

    private boolean hasNothingToDoWithAuthorizationHeader(final Parameter p) {
        return (!"AuthUserInfo".equalsIgnoreCase(p.getName()))
                && (!"Authorization".equalsIgnoreCase(p.getName()));
    }

    private boolean isNeedAuthorizationHeader(final HandlerMethod handlerMethod) {
        return Arrays.stream(handlerMethod.getMethodParameters())
                     .anyMatch(param ->
                             hasAuthUserAnnotation(param) || hasRequestHeaderWithFieldNameAccessToken(param)
                     );
    }

    private boolean hasAuthUserAnnotation(final MethodParameter param) {
        return param.getParameterAnnotation(AuthUser.class) != null;
    }

    private boolean hasRequestHeaderWithFieldNameAccessToken(final MethodParameter param) {
        return param.getParameterAnnotation(RequestHeader.class) != null
                && "Authorization".equalsIgnoreCase(param.getParameterAnnotation(RequestHeader.class).name())
                && "accessToken".equalsIgnoreCase(param.getParameter().getName());
    }

    private boolean hasSecurityRequirement(final Operation operation) {
        return operation.getSecurity() != null
                && operation.getSecurity().stream()
                            .anyMatch(securityRequirement ->
                                    securityRequirement.get(SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME) != null);
    }

    private Parameter customAuthHeaderParameter() {
        return new Parameter()
                .in("header")
                .schema(new StringSchema())
                .name(AUTHORIZATION_HEADER_CUSTOM_PARAMETER_NAME)
                .description("서비스 Access Token")
                .required(true)
                .example("Bearer access-token-abcdefghijklmn");
    }
}
