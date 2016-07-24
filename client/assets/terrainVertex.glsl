#version 120
attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;
attribute vec4 a_color;

uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_viewTrans;
uniform vec4 u_cameraPosition;
uniform vec3 u_cameraDirection;

varying vec2 texCoords;
varying vec4 v_diffuse;
varying float visibility;


vec3 lightPosition = vec3(10, 10, 10);
vec4 diffuse = vec4(2,2,2,1);

const float density = 0.02;
const float gradient = 2.5;
 
void main() {
	vec4 worldPosition = u_worldTrans * a_position;
    vec3 normal = normalize( a_normal);
    vec3 lightDir = normalize(lightPosition);
    float NdotL = max(dot(normal, lightDir), 0.0);

//    v_diffuse = u_ambient * NdotL;
    v_diffuse = diffuse * NdotL;

    texCoords = a_texCoord;

//    surfaceNormal = (u_worldTrans * vec4(normal, 0.0)).xyz;
//    toLightVector = lightPosition - worldPosition.xyz;

    float distance = length(u_cameraPosition.xyz - worldPosition.xyz);
    visibility = exp(-pow((distance * density), gradient));

    gl_Position = u_projViewTrans * a_position;
}
