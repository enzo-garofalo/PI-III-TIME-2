# 🛡️ SuperID — Gerenciador de Senhas e Login Seguro via QR Code

O **SuperID** é um aplicativo Android desenvolvido nativamente em **Kotlin**, projetado para oferecer uma experiência segura, rápida e inovadora na **gestão de senhas pessoais** e **autenticação sem senha** por meio de **QR Code**. Ele utiliza serviços do **Firebase** para garantir integridade, autenticação e escalabilidade.

> 🔐 **Login seguro nunca foi tão simples.**

---

## 🚀 Funcionalidades Principais

- 🔐 **Cadastro e Login Seguro**  
  Registro de usuários com **nome**, **email** e **senha mestre**, utilizando o Firebase Authentication com validação por email.

- 🗂️ **Gerenciamento de Senhas Pessoais**  
  Armazene, edite e remova senhas organizadas por categorias (como _Sites Web_, _Aplicativos_, etc).  
  🔒 Todas as senhas são **criptografadas** e protegidas com a senha mestre do usuário.

- 📲 **Login via QR Code em Sites Parceiros**  
  Acesse sites parceiros escaneando um **QR Code** com o app SuperID e autenticando com sua senha mestre — **sem precisar digitar sua senha no navegador**.

- ♻️ **Recuperação de Senha Mestre**  
  Redefina sua senha mestre por email, desde que validado previamente.

---

## 🌐 Site Parceiro

O **SuperID** possui um site parceiro, o **Webpi**, que foi desenvolvido para interagir diretamente com o aplicativo, permitindo a autenticação via QR Code em uma plataforma web. Confira o repositório do site:

- **Repositório Webpi**: [https://github.com/jgabrieldsl/webpi](https://github.com/jgabrieldsl/webpi)
- **Link do Site**: [https://webpi-nu.vercel.app/](https://webpi-nu.vercel.app/)
  
---

## ⚙️ Aspectos Técnicos

### 🔧 Tecnologias e Ferramentas

- **Kotlin** com Android SDK
- **Firebase Authentication**
- **Firebase Firestore**
- **Firebase Functions**
- **Criptografia AES/RSA** para proteção de dados
- **ML Kit Barcode Scanning** para leitura de QR Codes
- **Git e GitHub** com **GitFlow** para versionamento e colaboração

### 🔄 Fluxo de Desenvolvimento com GitFlow

O projeto utiliza o **GitFlow** como metodologia de versionamento para garantir um fluxo de trabalho organizado e colaborativo:

- **Branch `main`:** Contém a versão estável e pronta para produção.
- **Branch `develop`:** Integra as funcionalidades em desenvolvimento, servindo como base para testes e integração.
- **Branches de feature (`feature/*`):** Criadas para desenvolver novas funcionalidades, como gerenciamento de senhas ou autenticação via QR Code.
- **Branches de correção (`hotfix/*`):** Usadas para correções rápidas em produção.
- **Branches de release (`release/*`):** Preparação de versões para lançamento, com ajustes finais e testes.

Commits seguem o padrão **Conventional Commits** para maior clareza e rastreabilidade, e revisões são feitas via **Pull Requests** no GitHub.

### ✅ Requisitos Não Funcionais

- **Segurança:** Criptografia de ponta-a-ponta nas credenciais; QR Codes com tempo de expiração e uso único.
- **Desempenho:** Abertura rápida, navegação fluida e baixo consumo de recursos.
- **Usabilidade:** Interface intuitiva e responsiva com suporte ao **Jetpack Compose**.
- **Plataforma:** Aplicativo exclusivo para **Android** (mínimo API 33).

---

## 🛠️ Como Rodar o Projeto

1. Clone este repositório:  
   ```bash
   git clone https://github.com/enzo-garofalo/PI-III-TIME-2.git
   ```
2. Acesse a branch `develop` para a versão mais recente do desenvolvimento:  
   ```bash
   git checkout develop
   ```
3. Abra a pasta no **Visual Studio Code** ou **Android Studio** (recomendado).
4. Configure o Firebase conectando seu projeto ao console (siga o guia em `docs/firebase-setup.md`).
5. Compile e execute no emulador ou dispositivo físico.

---

## 👨‍💻 Contribuidores

<table>
  <tr>
    <td align="center"><b>Enzo Garofalo Pampana</b></td>
    <td align="center"><b>João Gabriel da Silva Leite</b></td>
    <td align="center"><b>Lucas Anelli Bissi</b></td>
    <td align="center"><b>Yuri Cardoso Balieiro</b></td>
  </tr>
</table>

---

## 🏁 Status do Projeto

✔️ Projeto Finalizado — **V1.0-final**  
📦 Organização com branches baseadas no **GitFlow** e uso de GitHub Projects para gerenciamento de tarefas.

---

## 📄 Licença

Este projeto é open-source e está licenciado sob a [MIT License](LICENSE).

---

## 📋 Dependências Principais

As principais dependências do projeto estão listadas no arquivo `build.gradle.kts`:

- **AndroidX e Jetpack Compose**: Para interface moderna e reativa.
- **Firebase**: Inclui `firebase-analytics`, `firebase-firestore-ktx`, `firebase-auth-ktx` (BOM 33.12.0).
- **CameraX**: Para captura e processamento de QR Codes (`camera-core:1.3.0`, `camera-camera2:1.3.0`, `camera-lifecycle:1.3.0`).
- **ML Kit Barcode Scanning**: Para leitura eficiente de QR Codes (`barcode-scanning:17.0.0`).
- **Kotlin Coroutines**: Para operações assíncronas.

Consulte o arquivo `build.gradle.kts` completo para detalhes.

---

> Feito com 💜 por alunos comprometidos com a inovação em segurança digital.
