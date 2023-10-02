# GitHubUserSearch

Use api GitHub [GitHub search](https://docs.github.com/en/free-pro-team@latest/rest/search/search?apiVersion=2022-11-28#search-code).

# Sample Page

Search page and liked page

|           User search           |   User like and sort button.    |
|:-------------------------------:|:-------------------------------:|
| ![main](images/search_page.png) | ![detail](images/like_page.png) |

# Architecture

MVVM patten and Manual dependency injection.

![architecture](images/architecture.webp)

# Use Library

all library info : [libs.versions.toml](gradle/libs.versions.toml)

- UI
    - Android navigation + fragment
    - AAC-ViewModel
    - ConstrainLayout
    - RecyclerView
- async
    - RxJava3
- Image loader
    - glide
- Test
    - Junit5
- Network
    - retrofit2
    - okhttp3

# License

```
Copyright 2018-2023 Tae-hwan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```