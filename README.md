# 백엔드 프로그램 실행 방법
## 1. 프로젝트를 자신의 컴퓨터로 가져오기
방법 1.1과 방법 1.2 중 하나를 선택해서 진행합니다.
### 1.1 Github를 이용해 프로젝트를 자신의 컴퓨터로 복사하기
git clone 명령은 github.com에 존재하는 Repository를 자신의 노트북 또는 PC로 복사하는 과정입니다.
- 터미널에서 다음 명령을 입력합니다.
```
git clone https://github.com/Favorite-School-Meal/favorite-school-meal-was.git
```
```
// clone한 폴더로 이동하는 방법
cd favorite-school-meal-was
```

### 1.2 압축 파일을 이용해 프로젝트를 자신의 컴퓨터로 복사하기
- LMS에 업로드된 압축 파일을 다운로드 받습니다.
- 다운 받은 압축 파일을 압축 해제합니다.

## 2. 통합 개발 환경(IDE)으로 가져오기
clone한 저장소를 자신이 사용하고 있는 통합 개발 환경(IDE)으로 가져오는 과정입니다.
Eclipse 또는 IntelliJ 등 자신이 사용하는 도구를 사용해서 진행해주세요. 이 문서에서는 IntelliJ를 기준으로 설명합니다.
- IntelliJ를 시작
- 다음 화면에서 `Import Project` 또는 `Open`을 선택합니다.
  ![image](https://github.com/Favorite-School-Meal/.github/assets/96174711/9eaf215e-fffa-4953-83e4-b88d309a589f)

- 앞에서 clone한 폴더 선택
- 다음 화면과 같이 `Import Project from external model` 또는 `Open File or Project` 선택 -> `Gradle` 선택(없을시 생략) -> Next(Ok) 또는 Finish 버튼을 클릭해 가져오기를 완료합니다.
![image](https://github.com/Favorite-School-Meal/favorite-school-meal-was/assets/96174711/62e71f19-ddf3-4553-bc16-0b395bbbe18e)


## 3. Java 버전 확인하기
- 터미널에서 `java -version`을 실행하여 Java 버전이 17인지 확인합니다. Eclipse 또는 IntelliJ와 같은 IDE에서 Java 17로 실행되는지 확인합니다.

## 4. 환경변수 설정하기
- 다음 사진과 같이 상단의 토글 버튼을 누른 후 "Edit Configurations..."를 클릭합니다.
  ![image](https://github.com/Favorite-School-Meal/favorite-school-meal-was/assets/96174711/c0fc3fd7-1dba-41a9-ad4c-34a589b38130)

- LMS에서 다운로드 받은 `environment_variables.txt` 파일을 복사하여 `Environment variables`에 입력합니다.

## 5. 프로젝트 실행하기
FavoriteSchoolMealApplication.java 파일을 열고 Run 버튼을 눌러 실행합니다.
![image](https://github.com/Favorite-School-Meal/favorite-school-meal-was/assets/96174711/53517af2-52b3-4920-a847-8217337ee7fc)





 
