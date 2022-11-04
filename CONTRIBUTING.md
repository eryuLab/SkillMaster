## GitHubの使い方
### 基本的な作業手順
1. issueを作成する
2. issueにassignする
3. 「自分の名前/作業内容」でブランチを切る
4. 作業する（コミット粒度はなるべく小さく）
5. issueを紐づけてプルリクを書く
6. レビューを依頼する

### issueの書き方
Issueタブより`New Issue`をクリックし、目的に応じたIssue作成ボタンをクリックする。  
必要事項を可能な限り記載し、Issueを作成する。

### ブランチについて
以下のブランチに関しては、削除をしないこと
* main
* develop

【mainにブランチについて】  
mainブランチは、いわば「正式版のプログラム」が完成したときに更新されるブランチです。

【developブランチについて】  
developブランチは、各個人が開発完了した時に更新されるブランチです

また、新しく作業を開始する場合は必ず以下の命名規則にしたがって、ブランチを作成してください。  
→ __**「自分の名前/作業内容」**__

### コミットについて
コミットは「１行プログラムを変更した」というように、細かい単位で行ってください。  
コミットメッセージは必ず以下のようにつけて下さい。
```
[add]: Test.javaの新規作成
```
`[add]:`のように内容の前にその内容が、どういった作業なのかを判別するもの（以下、コミット種別と表記）をつけて下さい。  
コミット種別一覧は以下の通りです。

| コミット種別       | つけるケース                                    |
| ------------ | ----------------------------------------- |
| `[add]:`     | プログラムファイルを新たに追加した時（例: Test.javaの追加）       |
| `[remove]: ` | プログラムファイルを削除した時                           |
| `[update]:`  | バグ以外の機能修正および機能追加(ただしファイルを追加した場合は`[add]:`) |
| `[fix]:`     | バグ修正                                      |
| `[hotfix]:`  | 重大なバグの修正                                  |
| `[create]:`  | config.ymlなどのリソースファイルの追加/作成               |
| `[delete]:`  | リソースファイルの削除                               |
| `[disable]:` | コメントアウトによる機能の無効化                          |
| `[rename]:`  | プログラムの名前を変えた場合                            |
| `[clean]:`   | ファイル移動などが伴うリファクタリングをした場合                  |
| `[upgrade]:` | patchを作成するとき（バージョンアップの時）                  |


### プルリクについて
プルリクを出す時のタイトルは、日本語で記載する。  
本文には「close #issue番号」のように、記載する。こうすることで、issueとの紐づけが自動的に完了する。  
基本的に、マージ先は「develop」を指定する。ただし、正式版のリリースの時は「main」を指定する。