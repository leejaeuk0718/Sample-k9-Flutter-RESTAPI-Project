@echo off
git checkout --orphan new_main
git add -A
git commit -m "초기 통합 및 설정: 프론트엔드, 백엔드 구조 세팅 및 README/체크리스트 문서 갱신 완료"
git branch -D main
git branch -M main
git push -f origin main
