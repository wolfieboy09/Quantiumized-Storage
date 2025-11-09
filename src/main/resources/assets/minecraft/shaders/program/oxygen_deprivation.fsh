#version 150

uniform sampler2D DiffuseSampler;
uniform float SpreadFactor;
uniform float Severity;

in vec2 texCoord;
out vec4 fragColor;

vec3 spread(vec3 color, float factor) {
    float mul = 1.0 + ((color.r + color.g + color.b) * 1.0 - 1.0) * factor;
    return color * mul;
}

vec3 desaturate(vec3 color, float factor) {
    vec3 luma = vec3(0.299, 0.587, 0.114);
    vec3 gray = vec3(dot(luma, color));
    return mix(color, gray, factor);
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    color.rgb = desaturate(spread(color.rgb, SpreadFactor * Severity), 0.3 * Severity);

    float vignette = smoothstep(0.3, 1.0, length(texCoord - 0.5));
    color.rgb *= mix(1.0, 0.4, vignette * Severity);

    fragColor = color;
}
