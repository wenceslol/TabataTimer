package com.tabatatimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import main.java.AudioPlayer; // Importação da classe AudioPlayer
import com.tabatatimer.AudioPlayer;



public class TabataTimer extends JFrame {
    // Configurações do Tabata (em segundos)
    private static final int DEFAULT_WORK_TIME = 20;
    private static final int DEFAULT_REST_TIME = 10;
    private static final int DEFAULT_ROUNDS = 8;
    private static final int DEFAULT_PREPARATION_TIME = 10;

    private static final int[] workTimeHolder = {DEFAULT_WORK_TIME};
    private static final int[] restTimeHolder = {DEFAULT_REST_TIME};
    private static final int[] roundsHolder = {DEFAULT_ROUNDS};
    private static final int[] preparationTimeHolder = {DEFAULT_PREPARATION_TIME};
    
    // Componentes da interface
    private JLabel timeLabel;
    private JLabel phaseLabel;
    private JLabel roundLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JProgressBar progressBar;
    
    // Janela visual do timer
    private TimerDisplayWindow displayWindow;
    
    // Variáveis de controle
    private Timer timer;
    private int currentTime;
    private int currentRound;
    private TimerPhase currentPhase;
    private boolean isRunning;
    
    // Enum para as fases do timer
    private enum TimerPhase {
        PREPARATION("PREPARAÇÃO", Color.ORANGE),
        WORK("TRABALHO", new Color(34, 139, 34)),
        REST("DESCANSO", new Color(30, 144, 255)),
        FINISHED("FINALIZADO", new Color(220, 20, 60));
        
        private final String displayName;
        private final Color color;
        
        TimerPhase(String displayName, Color color) {
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getDisplayName() { 
            return displayName; 
        }
        
        public Color getColor() { 
            return color; 
        }
    }
    
    // Classe para a janela de display visual
    private class TimerDisplayWindow extends JFrame {
        private JLabel displayTimeLabel;
        private JLabel displayPhaseLabel;
        private JLabel displayRoundLabel;
        private JPanel circularProgressPanel;
        private int maxTimeForProgress;
        private int currentTimeForProgress;
        
        public TimerDisplayWindow() {
            setTitle("Timer Display");
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setAlwaysOnTop(true);
            setResizable(true);
            
            initDisplayComponents();
            setupDisplayLayout();
            pack();
            
            // Posiciona a janela no canto superior direito com tamanho inicial maior
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(400, 500); // tamanho inicial maior
            setLocation(screenSize.width - getWidth() - 50, 50);
        }
        
        private void initDisplayComponents() {
            // Labels para o display - fontes dinâmicas baseadas no tamanho da janela
            displayTimeLabel = new JLabel("00:00", SwingConstants.CENTER);
            displayTimeLabel.setFont(new Font("Arial", Font.BOLD, 80));
            displayTimeLabel.setForeground(Color.WHITE);
            
            displayPhaseLabel = new JLabel("PRONTO", SwingConstants.CENTER);
            displayPhaseLabel.setFont(new Font("Arial", Font.BOLD, 32));
            displayPhaseLabel.setForeground(Color.WHITE);
            
            displayRoundLabel = new JLabel("Round: 0/8", SwingConstants.CENTER);
            displayRoundLabel.setFont(new Font("Arial", Font.BOLD, 24));
            displayRoundLabel.setForeground(Color.WHITE);
            
            // Listener para ajustar fontes quando a janela for redimensionada
            addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    adjustFontSizes();
                    circularProgressPanel.repaint();
                }
            });
            
            // Panel para progresso circular - ajusta dinamicamente
            circularProgressPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawCircularProgress(g);
                }
                
                @Override
                public Dimension getPreferredSize() {
                    // Calcula um tamanho preferido baseado no tamanho da janela
                    Container parent = getParent();
                    if (parent != null) {
                        int parentWidth = parent.getWidth();
                        int parentHeight = parent.getHeight();
                        int size = Math.min(parentWidth - 40, parentHeight - 200);
                        size = Math.max(size, 150); // tamanho mínimo
                        return new Dimension(size, size);
                    }
                    return new Dimension(200, 200);
                }
            };
            circularProgressPanel.setOpaque(false);
        }
        
        private void setupDisplayLayout() {
            setLayout(new BorderLayout());
            getContentPane().setBackground(Color.BLACK);
            
            // Panel principal
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBackground(Color.BLACK);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Panel superior com informações
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.BLACK);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            
            gbc.gridx = 0; gbc.gridy = 0;
            infoPanel.add(displayPhaseLabel, gbc);
            
            gbc.gridy = 1;
            infoPanel.add(displayTimeLabel, gbc);
            
            gbc.gridy = 2;
            infoPanel.add(displayRoundLabel, gbc);
            
            mainPanel.add(infoPanel, BorderLayout.NORTH);
            mainPanel.add(circularProgressPanel, BorderLayout.CENTER);
            
            add(mainPanel, BorderLayout.CENTER);
        }
        
        private void drawCircularProgress(Graphics g) {
            // AQUI: Corrigido o erro de digitação de Graphics2d para Graphics2D
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = circularProgressPanel.getWidth();
            int height = circularProgressPanel.getHeight();
            int size = Math.min(width, height) - 20;
            int x = (width - size) / 2;
            int y = (height - size) / 2;
            
            // Círculo de fundo
            g2d.setColor(new Color(60, 60, 60));
            g2d.setStroke(new BasicStroke(8));
            g2d.drawOval(x, y, size, size);
            
            // Círculo de progresso
            if (maxTimeForProgress > 0) {
                int progress = ((maxTimeForProgress - currentTimeForProgress) * 360) / maxTimeForProgress;
                g2d.setColor(currentPhase.getColor());
                g2d.setStroke(new BasicStroke(8));
                g2d.drawArc(x, y, size, size, 90, -progress);
            }
            
            // Indicador de tempo no centro com fonte dinâmica
            g2d.setColor(Color.WHITE);
            int fontSize = Math.max(16, size / 8); // tamanho mínimo de 16, máximo proporcional
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fm = g2d.getFontMetrics();
            String timeText = String.format("%d", currentTimeForProgress);
            int textX = x + (size - fm.stringWidth(timeText)) / 2;
            int textY = y + size / 2 + fm.getAscent() / 2;
            g2d.drawString(timeText, textX, textY);
        }
        
        public void updateDisplay(TimerPhase phase, int time, int round, int maxTime) {
            // Atualiza tempo
            int minutes = time / 60;
            int seconds = time % 60;
            displayTimeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            
            // Atualiza fase com cor
            displayPhaseLabel.setText(phase.getDisplayName());
            displayPhaseLabel.setForeground(phase.getColor());
            
            // Atualiza round
            displayRoundLabel.setText("Round: " + round + "/" + roundsHolder[0]);
            
            // Atualiza progresso circular
            currentTimeForProgress = time;
            maxTimeForProgress = maxTime;
            
            // Muda cor de fundo baseada na fase
            Color backgroundColor = new Color(
                (int)(phase.getColor().getRed() * 0.1),
                (int)(phase.getColor().getGreen() * 0.1),
                (int)(phase.getColor().getBlue() * 0.1)
            );
            getContentPane().setBackground(backgroundColor);
            
            circularProgressPanel.repaint();
        }
        
        private void adjustFontSizes() {
            // Ajusta o tamanho das fontes baseado no tamanho da janela
            int width = getWidth();
            int height = getHeight();
            
            // Calcula um fator de escala baseado no tamanho da janela
            double scaleFactor = Math.min(width / 400.0, height / 500.0);
            scaleFactor = Math.max(0.5, Math.min(2.0, scaleFactor)); // limita entre 50% e 200%
            
            // Ajusta as fontes
            int timeSize = (int)(80 * scaleFactor);
            int phaseSize = (int)(32 * scaleFactor);
            int roundSize = (int)(24 * scaleFactor);
            
            displayTimeLabel.setFont(new Font("Arial", Font.BOLD, timeSize));
            displayPhaseLabel.setFont(new Font("Arial", Font.BOLD, phaseSize));
            displayRoundLabel.setFont(new Font("Arial", Font.BOLD, roundSize));
        }
    }
    
    public TabataTimer() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        // Inicializa a janela de display ANTES de resetTimer
        displayWindow = new TimerDisplayWindow();
        
        resetTimer();
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initializeComponents() {
        setTitle("Tabata Timer - Controle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Labels principais
        timeLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 72));
        
        phaseLabel = new JLabel("PRONTO PARA COMEÇAR", SwingConstants.CENTER);
        phaseLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        roundLabel = new JLabel("Round: 0/" + roundsHolder[0], SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Botões
        startButton = new JButton("INICIAR");
        stopButton = new JButton("PAUSAR");
        resetButton = new JButton("RESETAR");
        
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        stopButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        stopButton.setEnabled(false);
        
        // Barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Pronto");
        
        // Timer principal (atualiza a cada segundo)
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel principal com informações
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Fase atual
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2;
        mainPanel.add(phaseLabel, gbc);
        
        // Tempo
        gbc.gridy = 1;
        mainPanel.add(timeLabel, gbc);
        
        // Round atual
        gbc.gridy = 2;
        mainPanel.add(roundLabel, gbc);
        
        // Barra de progresso
        gbc.gridy = 3; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(progressBar, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Informações do Tabata
        JPanel infoPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        infoPanel.setPreferredSize(new Dimension(200, 200));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Configuração Tabata"));
        
        infoPanel.add(new JLabel("Trabalho (s):"));
        JSpinner workSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_WORK_TIME, 1, 600, 1));
        workSpinner.addChangeListener(e -> {
            workTimeHolder[0] = (Integer) workSpinner.getValue();
            updateRoundLabel();
        });
        infoPanel.add(workSpinner);

        infoPanel.add(new JLabel("Descanso (s):"));
        JSpinner restSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_REST_TIME, 1, 600, 1));
        restSpinner.addChangeListener(e -> {
            restTimeHolder[0] = (Integer) restSpinner.getValue();
        });
        infoPanel.add(restSpinner);

        infoPanel.add(new JLabel("Rounds:"));
        JSpinner roundsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_ROUNDS, 1, 20, 1));
        roundsSpinner.addChangeListener(e -> {
            roundsHolder[0] = (Integer) roundsSpinner.getValue();
            updateRoundLabel();
        });
        infoPanel.add(roundsSpinner);

        infoPanel.add(new JLabel("Preparação (s):"));
        JSpinner prepSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_PREPARATION_TIME, 0, 60, 1));
        prepSpinner.addChangeListener(e -> {
            preparationTimeHolder[0] = (Integer) prepSpinner.getValue();
        });
        infoPanel.add(prepSpinner);        
        add(infoPanel, BorderLayout.EAST);
    }
    
    private void updateRoundLabel() {
        roundLabel.setText("Round: " + currentRound + "/" + roundsHolder[0]);
    }
    
    private void setupEventListeners() {
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseTimer();
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });
    }
    
    private void startTimer() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            
            // Mostra a janela de display quando iniciar
            displayWindow.setVisible(true);
            
            // Se está começando do zero, inicia com preparação
            if (currentPhase == TimerPhase.FINISHED || currentRound == 0) {
                currentPhase = TimerPhase.PREPARATION;
                currentTime = preparationTimeHolder[0];
                currentRound = 0;
                // Toca o som de preparação
                //AudioPlayer.playSound("bell.wav");
            }
            
            updateDisplay();
        }
    }
    
    private void pauseTimer() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
    
    private void resetTimer() {
        timer.stop();
        isRunning = false;
        currentTime = preparationTimeHolder[0];
        currentRound = 0;
        currentPhase = TimerPhase.FINISHED;
        
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        
        // Esconde a janela de display
        if (displayWindow != null) {
            displayWindow.setVisible(false);
        }
        
        updateDisplay();
        
        // Reset visual
        phaseLabel.setText("PRONTO PARA COMEÇAR");
        phaseLabel.setForeground(Color.BLACK);
        progressBar.setValue(0);
        progressBar.setString("Pronto");
    }
    
    private void updateTimer() {
        currentTime--;
        
        // Beep de aviso aos 3 segundos finais
        if (currentTime == 15 && currentPhase == TabataTimer.TimerPhase.WORK) {
            AudioPlayer.playSound("ding.wav");
        }

        if (currentTime <= 0) {
            // Avança para próxima fase
            advancePhase();
        }
        
        updateDisplay();
    }
    
    private void advancePhase() {
        switch (currentPhase) {
            case PREPARATION:
                // Toca o som de trabalho
                AudioPlayer.playSound("doublebell.wav");
                currentPhase = TimerPhase.WORK;
                currentTime = workTimeHolder[0];
                currentRound = 1;
                break;
                
            case WORK:
                if (currentRound < roundsHolder[0]) {
                    // Toca o som de descanso
                    AudioPlayer.playSound("bell.wav");
                    currentPhase = TimerPhase.REST;
                    currentTime = restTimeHolder[0];
                } else {
                    finishWorkout();
                    return;
                }
                break;
                
            case REST:
                // Toca o som de trabalho
                AudioPlayer.playSound("doublebell.wav");
                currentPhase = TimerPhase.WORK;
                currentTime = workTimeHolder[0];
                currentRound++;
                break;
                
            default:
                break;
        }
    }
    
    private void finishWorkout() {
        currentPhase = TimerPhase.FINISHED;
        currentTime = 0;
        timer.stop();
        isRunning = false;
        
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        
        // Toca o som de finalização
        AudioPlayer.playSound("finish.wav");
        
        // Esconde a janela de display
        displayWindow.setVisible(false);
        
        // Mensagem de conclusão
        JOptionPane.showMessageDialog(this, 
            "Treino concluído!", 
            "Treino Completo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateDisplay() {
        // Atualiza o tempo na janela principal
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        
        // Atualiza a fase
        phaseLabel.setText(currentPhase.getDisplayName());
        phaseLabel.setForeground(currentPhase.getColor());
        
        // Atualiza o round
        roundLabel.setText("Round: " + currentRound + "/" + roundsHolder[0]);
        
        // Atualiza a barra de progresso
        updateProgressBar();
        
        // Atualiza a janela de display se estiver visível
        if (displayWindow != null && displayWindow.isVisible()) {
            int maxTime = getMaxTimeForCurrentPhase();
            displayWindow.updateDisplay(currentPhase, currentTime, currentRound, maxTime);
        }
    }
    
    private int getMaxTimeForCurrentPhase() {
        switch (currentPhase) {
            case PREPARATION:
                return preparationTimeHolder[0];
            case WORK:
                return workTimeHolder[0];
            case REST:
                return restTimeHolder[0];
            default:
                return 0;
        }
    }
    
    private void updateProgressBar() {
        int maxTime;
        int progress;
        String progressText;
        
        switch (currentPhase) {
            case PREPARATION:
                maxTime = preparationTimeHolder[0];
                progress = maxTime > 0 ? ((preparationTimeHolder[0] - currentTime) * 100) / preparationTimeHolder[0] : 0;
                progressText = "Preparação: " + currentTime + "s";
                break;
                
            case WORK:
                maxTime = workTimeHolder[0];
                progress = ((workTimeHolder[0] - currentTime) * 100) / workTimeHolder[0];
                progressText = "Trabalho: " + currentTime + "s";
                break;
                
            case REST:
                maxTime = restTimeHolder[0];
                progress = ((restTimeHolder[0] - currentTime) * 100) / restTimeHolder[0];
                progressText = "Descanso: " + currentTime + "s";
                break;
                
            case FINISHED:
                progress = 100;
                progressText = "Finalizado!";
                break;
                
            default:
                progress = 0;
                progressText = "Pronto";
                break;
        }
        
        progressBar.setValue(progress);
        progressBar.setString(progressText);
    }
    
    public static void main(String[] args) {
        // Configuração do Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TabataTimer().setVisible(true);
            }
        });
    }
}