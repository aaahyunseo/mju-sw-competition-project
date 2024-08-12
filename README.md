# mju-sw-competition-project

## 📠 Convention

### 🤝 Branch Naming Convention

|  머릿말      | 설명        |
| ----------- | ---------- |
| master      | 서비스 브랜치    |
| develop     | 배포 전 작업 기준    |
| feature     | 기능 단위 구현    |
| hotfix      | 서비스 중 긴급 수정 건에 대한 처리   |

<details>
<summary>Branch Naming Convention Detail</summary>
<div markdown="1">

```
master(main) ── develop ── feature
└── hotfix
```
- [ ] [깃 플로우](https://techblog.woowahan.com/2553/)를 베이스로 하여 프로젝트 사이즈에 맞게 재정의했습니다.
- [ ] 브랜치 이름은 `cabab-case`를 따릅니다.

#### master(main)
- [ ] 실제 서비스가 이루어지는 브랜치입니다.
- [ ] 이 브랜치를 기준으로 develop 브랜치가 분기됩니다.
- [ ] 배포 중, 긴급하게 수정할 건이 생길시 hotfix 브랜치를 만들어 수정합니다.

#### develop
- [ ] 개발, 테스트, 릴리즈 등 배포 전 작업의 기준이 되는 브랜치입니다.
- [ ] 해당 브랜치를 default로 설정합니다.
- [ ] 이 브랜치에서 feature 브랜치가 분기됩니다.

#### feature
- [ ] 개별 개발자가 맡은 작업을 개발하는 브랜치입니다.
- [ ] feature/(feature-name) 과 같이 머릿말을 feature, 꼬릿말을 개발하는 기능으로 명명합니다.
- [ ] feature-name의 경우 cabab-case를 따릅니다.
- [ ] ex) feature/login-validation

#### hotfix
- [ ] 서비스 중 긴급히 수정해야 할 사항이 발생할 때 사용합니다.
- [ ] master에서 분기됩니다.

</div>
</details>

### 🤝 Commit Convention

|  머릿말     | 설명        |
| ----------- | ---------- |
| feat        | 기능 구현, 추가   |
| setting     | 패키지 설치, 개발 설정    |
| refactor    | 코드 리팩터링    |
| fix         | 버그 수정, 예외 케이스 대응, 기능 개선   |
| docs        | README.md 작성, 주석 작성   |
| chore       | 기타 작업  |

<details>
<summary>Commit Convention Detail</summary>
<div markdown="1">

- [ ] `feat: 회원가입 API 구현`과 같이 `머릿말: 내용` 형식으로 작성합니다.
- [ ] 리팩터링의 경우 기능의 변화 없이 구조를 개선할 때 사용됩니다. (ex: 입력 상태값을  커스텀 훅으로 분리)
- [ ] 여러 작업을 동시에 실행한 경우 한 줄에 한 내용씩 입력합니다. 가장 메인이 된 작업을 먼저 기입합니다.
```
- ❌ 잘못된 예시_1
feat: 버튼 컴포넌트 구현, API 중복 요청 현상 해결

- ❌ 잘못된 예시_2
feat: 버튼 컴포넌트 구현 || fix: API 중복 요청 현상 해결

- ⭕ 올바른 예시
feat: 버튼 컴포넌트 구현
fix: API 중복 요청 현상 해결
```

</div>
</details>
