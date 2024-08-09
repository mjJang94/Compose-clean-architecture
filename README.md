# Android - Compose Clean Architecture - Kotlin
Compose Clean Architecture 앱은 Naver 검색 API를 활용하여 검색어와 관련된 뉴스 리스트를 보여주는 간단한 스터디 목적의 앱입니다.   
또한 클린 아키텍처 원칙을 안드로이드 프로젝트에 적용해 보고, Kotlin 프로그래밍 언어의 기능을 최대한 활용해보는 것을 목표로 하고 있습니다.


# Application Scope
* Home Screen - 검색어 입력을 통해 관련된 뉴스 기사들을 볼 수 있습니다. 보관 하고 싶은 뉴스 기사들을 스크랩할 수 있으며, 스크랩한 기사들은 리스트로 모아 볼 수 있습니다.
* Detail Screen - 웹뷰를 통해 원본 뉴스 기사 보기를 지원합니다.


# Tech-Stack
이 프로젝트는 Android 개발 관행과 많은 인기 있는 라이브러리 및 도구를 활용하고 학습하는것에 목적을 두고 있습니다.   
대부분의 라이브러리는 안정된 버전을 사용하며, 특별한 이유가 없는 한 비안정적인 의존성은 사용하지 않습니다.
* Tech-stack
    * 100% Kotlin
        * stable 2.0.10 적용 
        * Coroutines - 백그라운드 작업 수행
        * Kotlin Flow - 전반적인 모든 레이어의 데이터 흐름
        * Retrofit - 네트워킹
    * Jetpack
        * Compose - Android UI 툴킷
        * Navigation - 화면간 전환 및 정보전달
        * ViewModel - 생명주기를 인식하며 UI State를 저장하고 관리
        * Hilt - 의존성 주입
        * Paging3 - 대용량 리스트 페이징 처리
* Architecture
  * 클린 아키텍처
  * Navigation을 활용한 단일 액티비티 아키텍처
  * MVI
* Gradle
  * Gradle Kotlin DSL
  * Gradle Plugin
    * Android Gradle - 표준 Android Plugin  
    * 버전 카탈로그 - 의존성 통합 관리
* CI
  * Github Actions
    * 빌드 전 Clean
    * 빌드 작업 수행
    
# Project Modules
* data
  * Repository 구현
  * API 상호작용
  * data model -> domain model 변환
  * 데이터 서비스 or 서드 파티 데이터 서비스 포함
* domain
  * 비즈니스 모델 포함
  * 비즈니스 규칙 포함
  * Repository interface bridge
* presentation
  * View에 데이터 표현
  * UseCase를 통한 local,remote 데이터 처리


# Architecture
* View - 상태를 소비하고, 효과를 적용하며, 이벤트를 위임하는 Composable 화면입니다.   


* ViewModel - 해당 화면의 상태를 관리하고 감소시키는 AAC ViewModel입니다. 또한 UI 이벤트를 가로채고 부수 효과를 생성합니다. ViewModel의 생명주기 범위는 해당 화면의 Composable에 연결되어 있습니다.   


* Model - 데이터를 검색하는 저장소 클래스입니다. 클린 아키텍처 컨텍스트에서는 저장소를 활용하는 유즈케이스를 사용해야 합니다.   
  
![Alt text](https://camo.githubusercontent.com/4ecd49d0b4a0a67e5f6dff8dbd80317710559f47db83e0d17dd9447795a44131/68747470733a2f2f692e696d6775722e636f6d2f55587746626d762e706e67)

위 이미지에 나와있듯이 Composable Screen, ViewModel 간에 세 가지의 핵심 구성 요소가 있습니다.
* State - 해당 화면의 상태 내용을 포함하는 데이터 클래스입니다. 예를 들어 사용자 목록, 로딩 상태 등이 있습니다. 상태는 Compose 런타임 MutableState 객체로 노출되어 초기 값과 함께 지속적인 업데이트를 수신하는 유스케이스와 완벽하게 일치합니다.


* Event - UI에서 프레젠테이션 계층으로 콜백을 통해 보내지는 단순 객체입니다. 이벤트는 사용자가 발생시킨 UI 이벤트를 반영해야 합니다. 이벤트 업데이트는 MutableSharedFlow 타입으로 노출되며, 이는 StateFlow와 유사하고 구독자가 없는 경우 게시된 이벤트가 즉시 삭제되는 방식으로 작동합니다.


* Effect - UI에 영향을 미쳐야 하는 일회성 부작용 동작을 신호하는 단순 객체입니다. 예를 들어 탐색 동작 트리거, 토스트 표시, 스낵바 표시 등이 있습니다. 효과는 ChannelFlow로 노출되며, 각 이벤트가 단일 구독자에게 전달되는 방식으로 작동합니다. 채널 버퍼가 가득 차면 구독자가 나타날 때까지 이벤트 발행을 중단합니다.


각각의 screen과 flow들은 위에서 설명한대로 state, event, effect를 정의하는 Contract 클래스를 가집니다.
