package com.battleship.model.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.battleship.interfaces.IBoardLoader;
import com.battleship.model.board.Board;

/**
 * Lớp "ChallengeBoardLoader" biểu diễn tải bảng trò chơi từ file
 * 
 * @author Nguyen Viet Hoang
 * @version 1.0
 * @since 2025-04-27
 */

public class ChallengeBoardLoader implements IBoardLoader {
	
    // --- THUỘC TÍNH ---
	private int maxShots; // Số lượng bắn giới hạn
    private int maxTime; // Thời gian giới hạn
    private int crossCount; // Số lượng tấn công dẫu cộng
    private int randomCount; // Số lượng tấn công ngẫu nhiên
    private int diamondCount; // Số lượng tấn công hình thoi

    // --- GETTER & SETTER ---
    public int getMaxShots() { return maxShots; }
    public int getMaxTime() { return maxTime; }
    public int getCrossCount() { return crossCount; }
    public int getRandomCount() { return randomCount; }
    public int getDiamondCount() { return diamondCount; }

    /**
     * Hàm tải bảng trò chơi từ file
     * 
     * @param filePath Đường dẫn tới file chứa bảng trò chơi
     * @return Bảng trò chơi đã tải
     * @throws IOException Nếu xảy ra lỗi khi đọc file
     */
    @Override
    public Board loadBoard(String filePath) throws IOException {
        Board board = new Board();
        List<String> lines = new ArrayList<>(); // Danh sách các dòng trong file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Tạo bộ đệm để đọc file
            String line;
            while ((line = br.readLine()) != null) { // Đọc từng dòng
                if (!line.trim().isEmpty()) lines.add(line.trim()); // Nếu dòng không rỗng, thêm vào danh sách
            }
        }

        int i = 0; // Chỉ số dòng   
        // Đọc tàu
        while (i < lines.size()) {
            String[] parts = lines.get(i).split("\\s+"); // Tách dòng thành các phần
            if (parts.length == 4) { // Nếu số phần bằng 4
                int x = Integer.parseInt(parts[0]); // Tọa độ x
                int y = Integer.parseInt(parts[1]); // Tọa độ y
                int length = Integer.parseInt(parts[2]); // Độ dài tàu
                boolean isHorizontal = Boolean.parseBoolean(parts[3]); // Xác định tàu nằm ngang hay dọc
                board.addShip(x, y, length, isHorizontal); // Thêm tàu vào bảng
                i++;
            } else {
                break; // Nếu số phần không bằng 4, thoát khỏi vòng lặp 
            }
        }

        // Đọc số lượng bắn giới hạn
        if (i < lines.size()) {
            maxShots = Integer.parseInt(lines.get(i)); // Lấy số lượng bắn giới hạn
            i++;
        }

        // Đọc số lượng tấn công đặc biệt
        if (i < lines.size()) {
            String[] cross = lines.get(i).split("\\s+"); // Tách dòng thành các phần
            if (cross.length == 2) crossCount = Integer.parseInt(cross[1]); // Lấy số lượng tấn công dẫu cộng
            i++;
        }
        if (i < lines.size()) {
            String[] random = lines.get(i).split("\\s+"); // Tách dòng thành các phần
            if (random.length == 2) randomCount = Integer.parseInt(random[1]); // Lấy số lượng tấn công ngẫu nhiên
            i++;
        }
        if (i < lines.size()) {
            String[] diamond = lines.get(i).split("\\s+"); // Tách dòng thành các phần
            if (diamond.length == 2) diamondCount = Integer.parseInt(diamond[1]); // Lấy số lượng tấn công hình thoi
            i++;
        }

        // Đọc thời gian (định dạng mm:ss)
        if (i < lines.size()) {
            String[] timeParts = lines.get(i).split(":"); // Tách dòng thành các phần
            if (timeParts.length == 2) { // Nếu số phần bằng 2
                int minutes = Integer.parseInt(timeParts[0]); // Phút
                int seconds = Integer.parseInt(timeParts[1]); // Giây
                maxTime = minutes * 60 + seconds; // Tính thời gian giới hạn
            } else {
                // Nếu chỉ là số giây
                maxTime = Integer.parseInt(lines.get(i)); // Lấy thời gian giới hạn
            }
        }

        return board; // Trả về bảng trò chơi đã tải
    }

}
