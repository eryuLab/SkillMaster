# SkillMaster

[![Join us on Discord](https://img.shields.io/discord/745416925924032523.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.gg/sH8etgNBy7)
[![EasySpigotAPI](https://img.shields.io/badge/EasySpigotAPI-%E2%AC%85-4D4.svg)](https://github.com/sya-ri/EasySpigotAPI)
[![](https://img.shields.io/badge/commandAPI-%E2%86%90-green)](https://github.com/JorelAli/CommandAPI)

## 開発環境

- [Intellij IDEA](https://www.jetbrains.com/idea/) などの統合開発環境
- OpenJDK 17
- [Kotlin 1.7.10](https://github.com/JetBrains/kotlin/releases/tag/v1.7.10)
- [gradle 7.3.3](https://www.npackd.org/p/gradle/7.3.3)
- Spigot 1.18.2

## 前提プラグイン

- CommandAPI-8.4.1
- EasySpigotAPI-2.4.0
- kotlin-reflect-1.7.10
- kotlin-stdlib-1.7.10
- LuckPerms-Bukkit-5.4.30
- VoxelSniper-6.1.2
- WorldEdit-Bukkit-7.2.10

## ビルド方法

最初に、Java Development Kit (JDK) 17をインストールする必要があります。

### IntelliJ IDEA の画面からビルドする

IntelliJ IDEAを開発に使用している場合、プロジェクトをgradleプロジェクトとして読み込み、  
gradleタブから `Tasks -> build -> build` を実行すれば、`build/libs`フォルダにjarが出力されます。

## デバッグ環境の作成方法・デバッグ方法

### 作成方法

`docker`, `docker-compose` および`gradle`が実行可能であるとします。  
Linux/MacOS環境では、`prepare-docker.sh`、Windows環境では`prepare-docker.bat`を実行することで、デバック用のWaterfall&Paper環境を構築することができます。  
初回起動時にはPaperのビルドに時間がかかります。また、[Minecraft EULA](https://www.minecraft.net/ja-jp/eula)に同意する必要があるため、実行が中断されます。  
EULAに同意しデバックを続行する場合、`./docker/paper/serverfiles/eula.txt`を参照し、`eula=false`を`eula=true`に書き換えて下さい。  
サーバーを停止する場合、`docker-compose down`を実行してください。

### デバッグ方法

DockerマシンのIPアドレス(Linux等なら`localhost`)を`DOCKER_IP`と表記します。  
マイクラ上で`DOCKER_IP:25568`へとアクセスすることでデバッグ用サーバーに接続できます。  
`op`やコマンド実行などでPaperのコンソールにアクセスする必要がある場合、`papera`または`paperb`へのコンテナ名とともに、
`docker attach [CONTAINER_NAME]`を実行してください。コンテナ名は`docker ps`で確認できます。
コンソールからは`Ctrl+C`(Macの場合は`Command+C`)で抜けることができます。（サーバーは停止されません）

## プラグインに貢献する方法

[Contributing](CONTRIBUTING.md) を参照して下さい
