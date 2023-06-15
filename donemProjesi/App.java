package donemProjesi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

public class App implements Serializable {
    private List<User> users;
    private Exercise exercise;
    private User currentUser;

    
    public Exercise getExercise() {
		return exercise;
	}
    
    
	public void serializeObjects() {
	    try {
	        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data.ser"));
	        

	        outputStream.writeObject(users);
            outputStream.writeObject(exercise);

	        outputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}//???
    
    
	public App() {
        users = new ArrayList<>();
        exercise = new Exercise(); 
    }
	
	
    public void changeParameter(int a, int b, int n) {
    	// a ve b max 10 kabul eden ve a,b,n'i değiştiren fonksiyon
			if(a>10 || b>10) {
			System.out.println("Max parameter should be 10.");
			return;
		}
		exercise.setA(a);
		exercise.setB(b);
		exercise.setN(n);	
	} 
	
    
    public List<User> getUsers() {
    	// kullanıcıları getiren fonksiyon
        return users;
    }
        

    public boolean login(String username, String password) {
        // kullanıcı var mı diye kontrol edip, isim ve şifre uyuşuyor mu diye kontrol eden fonksiyon
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    
    public HashMap<String, Integer> generateExercise(int n) {
    	// soru oluşturan fonksiyon, soruyu ve cevabını bir hashmap'e kaydediyor ve onu döndürüyor
        HashMap<String, Integer> exerciseMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String question = exercise.getQuestion();
            int answer = exercise.getAnswer(); 
            exerciseMap.put(question, answer);
        }
        return exerciseMap;
    }
    

    public User getCurrentUser() {
    	//girili kullanıcıyı döndüren fonksiyon
        return currentUser;
    }
    
      
    public void saveParametersToCSV(String filename, int a, int b, int n) {
        // Parametreleri daha sonraki girişte kullanmak için bir CSV dosyasına kaydeden fonksiyon
        try (FileWriter writer = new FileWriter(filename)) {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("A,B,N\n");
            sb.append(a).append(",").append(b).append(",").append(n).append("\n");

            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    
    public String loadHighScoresDataFromCSV(String filename) {
    	//yüksek skorları okuyan fonksiyon
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    
    public void saveHighScoreToCSV(String filename, String playerName, double score) {
        // Yüksek skorları CSV'ye kaydeden fonksiyon
        File file = new File(filename);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<>();
            String line;
            boolean isExistingHighScore = false;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingPlayerName = parts[0];
                double existingScore = Double.parseDouble(parts[1]);

                if (playerName.equals(existingPlayerName)) {
                    // Eğer aynı oyuncunun kaydı varsa ve yeni skor kayıtlı skordan büyük veya eşitse, skoru günceliyor
                    if (score >= existingScore) {
                        line = playerName + "," + score;
                    }
                    isExistingHighScore = true;
                }

                lines.add(line);
            }

            bufferedReader.close();
            fileReader.close();

            if (!isExistingHighScore) {
                // Eğer oyuncunun kaydı yoksa, yeni skoru ekliyor
                lines.add(playerName + "," + score);
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String updatedLine : lines) {
                bufferedWriter.write(updatedLine + "\n");
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void loadParametersFromCSV(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
          scanner.nextLine();
//ilk satırı atlayıp sırayla a, b ve N parametrelerini ayarlıyor
            if (scanner.hasNextLine()) {
                String[] parameters = scanner.nextLine().split(",");
                int a = Integer.parseInt(parameters[0]);
                int b = Integer.parseInt(parameters[1]);
                int n = Integer.parseInt(parameters[2]);
                exercise.setA(a);
                exercise.setB(b);
                exercise.setN(n);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı.");
        }
    }
    
        
    public void saveExerciseResultsToCSV(String filename, String childName, Date startTime, HashMap<String, Integer> exerciseMap,
            HashMap<String, Long> answerTimes, HashMap<String, Boolean> answerResults) {
    	//bu kocaman fonksiyon sırayla aldığı değerleri rapora döküyor, isim, zaman, sorular, cevap verme süresi, vb.
    	try {

    		boolean fileExists = new File(filename).exists();

    		FileWriter writer = new FileWriter(filename, true);

    		if (!fileExists) {
    			writer.write("ChildName,StartTime,TotalTime,Question,Answer,AnswerTime,Result,Score,TotalScore\n");
    		}

    		long totalSolvingTime = 0;
    		for (long answerTime : answerTimes.values()) {
    				totalSolvingTime += answerTime;
    		}

    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		int questionNumber = 1;
    		int totalCorrectAnswers = 0;
    		
    		for (String question : exerciseMap.keySet()) {
    		if (answerResults.get(question)) {
                totalCorrectAnswers++;}}
    		
    		double totalScore = (double) totalCorrectAnswers / exercise.getN() * (exercise.getN() * 2 /totalSolvingTime )*100;
    		
    		for (String question : exerciseMap.keySet()) {
    				int correctAnswer = exerciseMap.get(question);
    				long answerTime = answerTimes.get(question);
    				boolean isCorrect = answerResults.get(question);
    				writer.write(childName + ",");
    				writer.write(dateFormat.format(startTime) + ","); // Format the start time as a date
    				writer.write(totalSolvingTime + ",");
    				writer.write(questionNumber + ",");
    				writer.write(question + ",");
    				writer.write(correctAnswer + ",");
    				writer.write(answerTime + ",");
    				writer.write(isCorrect ? "Correct" : "Incorrect" + ",");
    				double score = (isCorrect ? 1.0 : 0.0) * (answerTime / 2.0)*100/exercise.getN();
    				writer.write(score + ",");
    				writer.write(totalScore + "");
    				writer.write("\n");
    				questionNumber++;
    				
    	            }    		
    		
    		writer.close();

    	} 	
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }    
    
    
    public void logout() {
        currentUser = null; // kullanıcıyı sıfırlayıp çıkış 
    }
    
    
    public String loadStatisticsDataFromCSV(String filename) {
    	//bu fonksiyon ise kaydedilen raporları okumayı yapıyor
        StringBuilder statisticsData = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                statisticsData.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statisticsData.toString();
    }
    
}
