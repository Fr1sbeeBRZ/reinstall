#!/bin/bash

# Script for configure the system on a new instalation

#Git config
git config --global user.name "Fr1sbee"
git config --global user.email ikeralvitealmozaravilacerio@gmai.com

ssh-keygen -t ed25519 -C "ikeralvitealmozara@gmail.com"
#Clone repos
mkdir ~/repos
cd repos

pacman -Syu --noconfirm
# Paquetes que se instalan
# 2. InteliJ
# 3. Spotify
# 4. Flameshot (Capturas de pantalla)
# 5. Redshift (Filtro luz azul)
# 6. Mongo
# 7. VsCode

git clone https://aur.archlinux.org/intellij-idea-ce.git
git clone https://aur.archlinux.org/spotify.git
git clone https://aur.archlinux.org/flameshot-git.git
git clone https://aur.archlinux.org/redshift-git.git
git clone https://aur.archlinux.org/mongodb-bin.git
git clone https://aur.archlinux.org/mongosh-bin.git
git clone https://aur.archlinux.org/visual-studio-code-bin.git

# Update system
pacman -Syu --noconfirm 

#Install some utilitis 
pacman -S --noconfirm pamixer vivaldi brightnessctl xorg-xinit rofi yt-dlp audacity

#Create the Xprofile
touch ~/.xprofile
mkdir /etc/scripts/
#Copy files to his place

cp redshift.conf  ~/.config/redshift.conf
cp autostart.sh /etc/scripts/
cp .xprofile ~/