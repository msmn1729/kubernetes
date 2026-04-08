package com.example.demo;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
  private BoardRepository boardRepository;

  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public List<Board> getBoards() {
    return boardRepository.findAll();
  }

  public Board writeBoard(Board board) {
    return boardRepository.save(board);
  }
}

