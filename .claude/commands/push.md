---
description: 현재 local 브랜치를 remote 브랜치에 push합니다.
argument-hint: On/Off(Default = On)
---

push를 진행할 때 현재 local 브랜치가 remote 에 없다면 remote에 생성한다.

$ARGUMENTS 는 force push 여부를 나타내고 기본값은 On이다. 또한 대소문자 구분을 하지 않는다.
 
브랜치의 커밋 베이스가 충돌이 나서 push가 되지 않는 경우 $ARGUMENTS가 On이면 force push한다.