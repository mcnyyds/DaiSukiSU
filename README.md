# DaiSukiSU

基于 ReSukiSU `v4.2.0-rc3 (35203)` 的管理器语言定制版，使用「主人喵喵版」简体中文翻译。

对应上游提交：`8770c7e324a22895703c4916b8a16520e0b81c79`。
应用显示名称为 **DaiSukiSU**，包名为 `com.resukisu.resukisu.meow`，可与官方管理器并存。

## 构建 APK

使用 `meow-35203` 分支的 **Build DaiSukiSU 35203 (App only)** 工作流。
修改该分支的 `manager/` 文件并提交会自动构建。手动运行工作流需要该工作流已在仓库默认分支中。
如果 Fork 的 Actions 尚未启用，请先在 GitHub 的 Actions 页面启用。

在 **Settings → Secrets and variables → Actions** 配置：

| Secret | 内容 |
| --- | --- |
| `KEYSTORE` | 自己的 PKCS12/JKS 签名密钥文件的 Base64 |
| `KEYSTORE_PASSWORD` | 密钥库密码 |
| `KEY_ALIAS` | 签名密钥别名 |
| `KEY_PASSWORD` | 签名密钥密码 |

沿用已安装版本的密钥，才能覆盖更新。密钥及其密码只能放入 Secrets，不能提交进公开仓库。

构建完成后，在 Actions 运行页面的 **Artifacts** 下载 `DaiSukiSU-35203-arm64-v8a`。

本流程只编译管理器源码，复用官方 35203 ARM64 APK 的 `libadbroot.so`、`libkernelsu.so`、`libksud.so`，不编译内核或 LKM。
原生文件的固定校验值记录在 `.github/prebuilt-35203.sha256`；对应源码保留在本分支。
通过 Gradle 属性固定版本号和版本名称，提交翻译修改不会把版本号自动提高到 35204。

## 修改翻译

修改 `manager/app/src/main/res/values-zh-rCN/strings.xml`，保留资源名、格式占位符和换行转义。

## 安装

保留官方 ReSukiSU，安装 DaiSukiSU 后，在官方管理器的「设置 → 动态管理器配置」中选择 DaiSukiSU 并授权。
需要设备内核支持动态管理器；自签名 APK 不能覆盖官方签名的应用。

原项目及许可证信息见 [上游说明](docs/README.md) 和 [LICENSE](LICENSE)。
