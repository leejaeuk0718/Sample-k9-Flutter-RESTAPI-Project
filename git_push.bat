@echo off
git remote remove origin 2>nul
git add .
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/lsy3709/Sample-k9-Flutter-RESTAPI-Project.git
git push -u origin main
