package donemProjesi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GUI {
    private App gameApp;
    protected JFrame frame;
    private int currentQuestionIndex;
    protected List<String> questions = new ArrayList<>();
    private HashMap<String, Integer> exerciseMap;
    private HashMap<String, Boolean> answerResults;
    private HashMap<String, Long> answerTimes;
    private AtomicLong startTime = new AtomicLong();
        
    public GUI(App gameApp) {
        this.gameApp = gameApp;
        this.currentQuestionIndex = 0;
    }
  
    public void run() {
        // dosyadan parametreleri okuyarak en son kaydedilen açılıyor
        gameApp.loadParametersFromCSV("parameters.csv");

        frame = new JFrame("Çarpım Tablosu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLayout(new FlowLayout());

        // giriş 
        JButton loginButton = new JButton("Giriş yapmak için tıklayınız.");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginScreen();
            }
        });
        
        JLabel textLabel = new JLabel("Parent: natalia");
        JLabel textLabel2 = new JLabel("Children: melisa ve melis");
        JLabel textLabel3 = new JLabel("Şifreleri melisa123");
        
        frame.setLayout(new FlowLayout());
        frame.add(loginButton);
        frame.add(textLabel);
        frame.add(textLabel2);
        frame.add(textLabel3);
        frame.setVisible(true);
    }
    
    public void LoginScreen() {
        frame.getContentPane().removeAll();


        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);


        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);


        JButton loginButton = new JButton("Giriş");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean loginSuccessful = gameApp.login(username, password);
                if (loginSuccessful) {

                    UserType userType = gameApp.getCurrentUser().getUserType();
                    if (userType == UserType.PARENT) {
                        // eğer kullanıcı parent ise ilgili sekmeyi açıyor
                        showParentScreen();
                    } else if (userType == UserType.CHILD) {
                        // aynı şekilde kullanıcı çocuksa ilgili pencereyi açıyor
                        generateQuestions();
                        showChildScreen();
                    }
                } else {
                    // eğer giriş hatalıysa uyarı gösteriyor
                    JOptionPane.showMessageDialog(frame, "Hatalı kullanıcı ismi ya da şifre.", "Başarısız", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exitButton = new JButton("Çıkış");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //kapatma tuşu 
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(exitButton);
        frame.revalidate();
        frame.repaint();
    }

    public void showParentScreen() {
    	//burda her sekme için farklı bir fonksiyon çağırılıyor
        frame.getContentPane().removeAll();


        JButton changeParametersButton = new JButton("Parametreleri değiştir");
        changeParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showParameterSelectionScreen();
            }
        });

        JButton viewStatisticsButton = new JButton("Raporları gör");
        viewStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });

        JButton viewHighScoresButton = new JButton("Yüksek skorlar");
        viewHighScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });
        JButton logoutButton = new JButton("Hesaptan çık");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameApp.logout();
                LoginScreen();
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(changeParametersButton);
        frame.add(viewStatisticsButton);
        frame.add(viewHighScoresButton);
        frame.add(logoutButton);
        frame.revalidate();
        frame.repaint();
    }

    public void showHighScores() {
        frame.getContentPane().removeAll();

        JTextArea highScoresTextArea = new JTextArea(10, 20);
        highScoresTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(highScoresTextArea);

        JButton backButton = new JButton("Geri");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showParentScreen();
            }
        });
        //yüksek skorları app'teki fonksiyon ile çağırıyor
        String highScoresData = gameApp.loadHighScoresDataFromCSV("highscores.csv");
        highScoresTextArea.setText(highScoresData);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
    
    public void showParameterSelectionScreen() {
        frame.getContentPane().removeAll();

//burda da a, b ve n'i alıp app'teki fonksiyonları kullanarak değiştiriyor ve kaydediyor
        JLabel aLabel = new JLabel("A:");
        JTextField aField = new JTextField(5);
        JLabel bLabel = new JLabel("B:");
        JTextField bField = new JTextField(5);
        JLabel nLabel = new JLabel("N:");
        JTextField nField = new JTextField(5);
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = Integer.parseInt(aField.getText());
                int b = Integer.parseInt(bField.getText());
                int n = Integer.parseInt(nField.getText());


                gameApp.changeParameter(a, b, n);
                JOptionPane.showMessageDialog(frame, "Parametreler başarıyla değişti.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                gameApp.saveParametersToCSV("parameters.csv",a,b,n); 
                showParentScreen();
            }
        });


        frame.setLayout(new FlowLayout());
        frame.add(aLabel);
        frame.add(aField);
        frame.add(bLabel);
        frame.add(bField);
        frame.add(nLabel);
        frame.add(nField);
        frame.add(saveButton);
        frame.revalidate();
        frame.repaint();
    }

    public void showStatistics() {
        frame.getContentPane().removeAll();


        JTextArea statisticsTextArea = new JTextArea(10, 20);
        statisticsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statisticsTextArea);


        JButton backButton = new JButton("Geri");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showParentScreen();
            }
        });
//appteki fonksiyon ile istatistikleri okuyor
        String statisticsData = gameApp.loadStatisticsDataFromCSV("statistics.csv");
        statisticsTextArea.setText(statisticsData);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    public void resetExerciseData() {
    	//kullanıcılar arası girip çıkarken sıfırlamak gerekiyor
        exerciseMap.clear();
        answerTimes.clear();
        answerResults.clear();
        questions.clear();
        currentQuestionIndex = 0;
    }
    
    public void showChildScreen() {
        frame.getContentPane().removeAll();
//başlata bastığı zaman egzersizler başlıyor
        JButton startGameButton = new JButton("Başlat");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExerciseScreen();
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(startGameButton);
        frame.revalidate();
        frame.repaint();
    }

    public void showExerciseScreen() {
        frame.getContentPane().removeAll();

        JLabel questionLabel = new JLabel(questions.get(currentQuestionIndex));
        JTextField answerField = new JTextField(10);
        JButton submitButton = new JButton("Gönder");
        JLabel timerLabel = new JLabel("Zaman: 0 saniye");

        startTime.set(System.currentTimeMillis());

        Timer timer = new Timer(1000, new ActionListener() {
            int timeInSeconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeInSeconds++;
                timerLabel.setText("Zaman: " + timeInSeconds + " saniye");
            }
        });
        timer.start();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                timer.stop();

                long endTime = System.currentTimeMillis();

                // saniyeye çevirmek için /1000
                long answerTime = (endTime - startTime.get()) / 1000;

                String currentQuestion = questions.get(currentQuestionIndex);
                int childAnswer = Integer.parseInt(answerField.getText());
                int correctAnswer = exerciseMap.get(currentQuestion);

                boolean isCorrect = (childAnswer == correctAnswer); 

                if (isCorrect) {
                    JOptionPane.showMessageDialog(frame, "Doğru!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Yanlış!");
                }

                answerResults.put(currentQuestion, isCorrect);
                answerTimes.put(currentQuestion, answerTime);

                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    questionLabel.setText(questions.get(currentQuestionIndex));
                    answerField.setText("");

                    startTime.set(System.currentTimeMillis());
                    timer.restart();
                } else {

                    String childName = gameApp.getCurrentUser().getUsername();
                    gameApp.saveExerciseResultsToCSV("statistics.csv", childName, new Date(startTime.get()), exerciseMap, answerTimes, answerResults);

                    showResultScreen();
                }
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(questionLabel);
        frame.add(answerField);
        frame.add(submitButton);
        frame.add(timerLabel);
        frame.revalidate();
        frame.repaint();
    }

    public void showResultScreen() {
        frame.getContentPane().removeAll();
        int totalCorrectAnswers = 0;
        long totalSolvingTime = 0;

        for (String question : exerciseMap.keySet()) {
            if (answerResults.get(question)) {
                totalCorrectAnswers++;
            }
            totalSolvingTime += answerTimes.get(question);
        }
//skoru doğru sayısı oranı ve hıza göre buluyorum, soru başına iki saniyeyi standart gibi düşünerek
        double totalScore = (double) totalCorrectAnswers / (double) gameApp.getExercise().getN() * ( (double) gameApp.getExercise().getN() * 2 /totalSolvingTime )*100;
        JLabel resultLabel = new JLabel("Sorular tamamlandı!");
        JLabel scoreLabel = new JLabel("Toplam skor: " + totalScore);
     // yüksek skorları kaydet
        String childName = gameApp.getCurrentUser().getUsername();
        gameApp.saveHighScoreToCSV("highscores.csv", childName, totalScore);
        
        JButton playAgainButton = new JButton("Tekrar dene");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentQuestionIndex = 0;
                resetExerciseData();
                generateQuestions(); 
                showChildScreen();
            }
        });

        // Log out 
        JButton logoutButton = new JButton("Hesaptan çık");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameApp.logout();
            	resetExerciseData();
                LoginScreen();
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(resultLabel);
        frame.add(scoreLabel);
        frame.add(playAgainButton);
        frame.add(logoutButton);
        frame.revalidate();
        frame.repaint();
    }

    public void generateQuestions() {
        exerciseMap = new HashMap<>();
        answerResults = new HashMap<>();
        answerTimes = new HashMap<>(); 
        Exercise exercise = gameApp.getExercise(); 
        int n = exercise.getN();
        exerciseMap = gameApp.generateExercise(n);

        for (String question : exerciseMap.keySet()) {
            answerTimes.put(question, 0L);
        }
        int count = 0; 
        Iterator<String> questionIterator = exerciseMap.keySet().iterator();

        while (questionIterator.hasNext() && count < exercise.getN()) {
            String question = questionIterator.next();
            questions.add(question);
            answerTimes.put(question, 0L);
            answerResults.put(question, false);
            count++;
        }
        }



}