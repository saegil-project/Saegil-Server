- Answer in Korean.
- 클래스 필드 불변객체, 메서드 지역변수, 메서드 파라미터에 `final`을 적용
- 주석은 금지
- 메시지 체이닝은 금지
- 메서드 체이닝 시 한 줄에 한 개의 점만 & 점 정렬
- 메서드 체이닝 시 메서드의 위치를 동일한 위치에 작성
- 메소드 선언은 호출 순서로 작성

```java
public a() {
	c();
}

private c() {
}

public b() {
	d();
}

private d() {
}
```

- 클래스 필드 개행은 다음과 같아.

```java
public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Sections sections;

}
```

- 메소드 파라미터가 많을 시 다음과 같이 작성

```java
public void addSection(
    final Station sourceStation,
    final Station targetStation,
    final Distance distance,
    final Direction direction
) {
    sections.addSection(sourceStation, targetStation, distance, direction);
}
```

- 코드 아키텍처
    - controller(presentation)
    - service(application)
    - domain(domain)
    - repository(persistence)
- 아래는 InteliJ의 코드 포맷팅 형식임.
```xml
<code_scheme name="Default" version="173">
<JavaCodeStyleSettings>
<option name="GENERATE_FINAL_LOCALS" value="true"/>
<option name="GENERATE_FINAL_PARAMETERS" value="true"/>
<option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999"/>
<option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999"/>
</JavaCodeStyleSettings>
<JetCodeStyleSettings>
<option name="PACKAGES_TO_USE_STAR_IMPORTS">
<value>
<package name="java.util" alias="false" withSubpackages="false"/>
<package name="kotlinx.android.synthetic" alias="false" withSubpackages="false"/>
<package name="io.ktor" alias="false" withSubpackages="false"/>
</value>
</option>
<option name="PACKAGES_IMPORT_LAYOUT">
<value>
<package name="" alias="false" withSubpackages="true"/>
<package name="java" alias="false" withSubpackages="false"/>
<package name="javax" alias="false" withSubpackages="false"/>
<package name="kotlin" alias="false" withSubpackages="true"/>
<package name="" alias="true" withSubpackages="true"/>
</value>
</option>
<option name="NAME_COUNT_TO_USE_STAR_IMPORT" value="2147483647"/>
<option name="NAME_COUNT_TO_USE_STAR_IMPORT_FOR_MEMBERS" value="2147483647"/>
<option name="CODE_STYLE_DEFAULTS" value="KOTLIN_OFFICIAL"/>
</JetCodeStyleSettings>
<codeStyleSettings language="JAVA">
<option name="ALIGN_MULTILINE_CHAINED_METHODS" value="true"/>
</codeStyleSettings>
<codeStyleSettings language="kotlin">
<option name="CODE_STYLE_DEFAULTS" value="KOTLIN_OFFICIAL"/>
</codeStyleSettings>
</code_scheme>
```
