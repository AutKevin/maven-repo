# maven-repo

aeolian工具合集

### Maven仓库

 典型的一个maven依赖下会有这三个文件： 

```json
maven-metadata.xml 
maven-metadata.xml.md5 
maven-metadata.xml.sha1 
```

`maven-metadata.xml`里面记录了最后deploy的版本和时间。  md5、sha1校验文件是用来保证这个meta文件的完整性。 

maven在编绎项目时，会先尝试请求maven-metadata.xml，如果没有找到，则会直接尝试请求到jar文件，在下载jar文件时也会尝试下载jar的md5, sha1文件。

maven-metadata.xml文件很重要，如果没有这个文件来指明最新的jar版本，那么即使远程仓库里的jar更新了版本，本地maven编绎时用上-U参数，也不会拉取到最新的jar！

## 发布

#### 生成Token

点击右上角头像，点击Setting -》 Developer settings -》Personal access tokens，写上备注，勾上repo和package的权限。生成后一定要先复制Token，Token只会在刚生成的时候显示一次。

#### settings.xml

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <!-- 发布读取都要用到,id一定要和pom中的一致 -->
  <servers>
    <server>
      <id>github</id>
      <username>USERNAME,这里是settings中的name</username>
      <password>TOKEN,注意这里是Token不是你的密码!!!</password>
    </server>
  </servers>
</settings>
```

#### pom.xml

```xml
<!--发布到github package,一定要注释掉maven-deploy-plugin插件-->
<distributionManagement>
    <repository>
        <id>github</id>  <!--这里要和后台的server的id一致-->
        <name>GitHub AutKevin Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/AutKevin/maven-repo</url>
    </repository>
</distributionManagement>
```

## 使用GitHub package

#### pom.xml

先引用仓库

```xml
    <repository>
      <id>github</id>
      <name>GitHub AutKevin Apache Maven Packages</name>
      <url>https://maven.pkg.github.com/AutKevin/maven-repo</url>
    </repository>
```
每个项目都配置会很麻烦，可以在settings.xml中配置全局仓库

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <!-- 读取mvn库用,选择要激活的profile -->
  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
  <!-- 读取mvn库用,配置一系列profile,一定要写到具体仓库 -->
  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github</id>
          <name>GitHub AutKevin Apache Maven Packages</name>
          <url>https://maven.pkg.github.com/AutKevin/maven-repo</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <!-- 上面两项也可以不配置,只不过每次都要在pom.xml文件中配置,这里配置可以一劳永逸 -->
</settings>
```

或者使用自己vsftp+tomcat搭建的仓库

```xml
<repository>
    <id>tomcata-repository</id>
    <url>http://52zt.info:88/mvn</url>
</repository>
```

#### 添加依赖

```xml
<dependency>
    <groupId>com.autumn</groupId>
    <artifactId>aeo-tool</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 引用本地仓库

 可以把本项目clone到本地，编译后直接引入本地file仓库，pom.xml配置如下。

```xml
<repository>
    <id>local-maven</id>
    <url>file:D:\ProgramFiles_QY\IdeaProjects\maven-repo\target\mvn-repo</url>
</repository>
```



