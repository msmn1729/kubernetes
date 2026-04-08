package com.example.demo;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("boards")
public class BoardController {

  private BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping()
  public List<Board> getBoards(
  ) {
    return boardService.getBoards();
  }

  @PostMapping()
  public Board writeBoard(@RequestBody Board board) {
    return boardService.writeBoard(board);
  }
}