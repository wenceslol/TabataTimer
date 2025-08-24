# Tabata Timer

Um aplicativo desktop desenvolvido em Java com Swing para treinos de Tabata, oferecendo interface intuitiva e display visual avançado para acompanhar seus exercícios.

## 📋 Funcionalidades

### Interface Principal
- **Timer visual** com display de minutos e segundos
- **Indicador de fase atual** (Preparação, Trabalho, Descanso, Finalizado)
- **Contador de rounds** mostrando progresso atual
- **Barra de progresso** com informações em tempo real
- **Controles principais**: Iniciar, Pausar e Resetar

### Janela de Display Flutuante
- **Display sempre no topo** para visualização durante exercícios
- **Interface redimensionável** com fontes que se ajustam automaticamente
- **Progresso circular visual** com cores dinâmicas baseadas na fase
- **Fundo com cores temáticas** que mudam conforme a fase do treino
- **Posicionamento inteligente** no canto superior direito da tela

### Configurações Personalizáveis
- **Tempo de trabalho**: Configure de 1 a 600 segundos
- **Tempo de descanso**: Configure de 1 a 600 segundos  
- **Número de rounds**: Configure de 1 a 20 rounds
- **Tempo de preparação**: Configure de 0 a 60 segundos

### Sistema de Áudio
- **Sinais sonoros** para transições entre fases:
  - Som de preparação ao iniciar
  - Som diferenciado para início do trabalho
  - Som de descanso entre rounds
  - Aviso sonoro aos 15 segundos finais do trabalho
  - Som especial de finalização do treino

### Cores e Visual
- **Preparação**: Laranja
- **Trabalho**: Verde
- **Descanso**: Azul
- **Finalizado**: Vermelho

## 🚀 Como Compilar e Executar

### Pré-requisitos
- Java JDK 8 ou superior
- Apache Maven 3.6 ou superior

### Estrutura do Projeto
```
tabata-timer/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── tabatatimer/
│                   ├── TabataTimer.java
│                   └── AudioPlayer.java
├── src/
│   └── main/
│       └── resources/
│           └── sounds/
│               ├── bell.wav
│               ├── doublebell.wav
│               ├── ding.wav
│               └── finish.wav
├── pom.xml
└── README.md
```

### Compilação com Maven

1. **Clone o repositório**:
```bash
git clone https://github.com/seu-usuario/tabata-timer.git
cd tabata-timer
```

2. **Compile o projeto**:
```bash
mvn clean compile
```

3. **Execute o aplicativo**:
```bash
mvn exec:java -Dexec.mainClass="com.tabatatimer.TabataTimer"
```

### Gerando JAR Executável

Para criar um JAR executável:

```bash
mvn clean package
```

O JAR será gerado em `target/tabata-timer-1.0-SNAPSHOT.jar`

Para executar o JAR:

```bash
java -jar target/tabata-timer-1.0-SNAPSHOT.jar
```

## 🎯 Como Usar

1. **Configure seu treino** usando os spinners no painel direito:
   - Defina o tempo de trabalho (padrão: 20s)
   - Defina o tempo de descanso (padrão: 10s)
   - Escolha o número de rounds (padrão: 8)
   - Configure o tempo de preparação (padrão: 10s)

2. **Inicie o treino** clicando em "INICIAR"
   - A janela de display flutuante aparecerá automaticamente
   - O timer começará com a fase de preparação

3. **Acompanhe o progresso**:
   - Interface principal mostra todas as informações
   - Janela flutuante oferece visualização otimizada durante exercícios
   - Sinais sonoros indicam mudanças de fase

4. **Controle o treino**:
   - Use "PAUSAR" para interromper temporariamente
   - Use "RESETAR" para voltar ao estado inicial
   - A janela de display pode ser redimensionada conforme necessário

## 📁 Arquivos de Áudio

O projeto requer os seguintes arquivos de áudio na pasta `src/main/resources/sounds/`:

- `bell.wav` - Som de preparação e descanso
- `doublebell.wav` - Som de início do trabalho
- `ding.wav` - Aviso aos 15 segundos finais
- `finish.wav` - Som de finalização

## 🔧 Desenvolvimento

### Dependências
O projeto utiliza apenas bibliotecas padrão do Java:
- `javax.swing.*` - Interface gráfica
- `java.awt.*` - Componentes visuais
- Classes padrão para manipulação de áudio

### Arquitetura
- **TabataTimer.java**: Classe principal com interface e lógica do timer
- **TimerDisplayWindow**: Classe interna para janela flutuante
- **AudioPlayer.java**: Classe para reprodução de sons
- **TimerPhase**: Enum para controle das fases do treino

## 📝 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

**Nota**: Certifique-se de que os arquivos de áudio estejam presentes na pasta de recursos para o funcionamento completo do aplicativo.
