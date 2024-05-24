package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String boards(Model model, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "list";
    }

    @GetMapping("/view/{id}")
    public String showView(Model model, @PathVariable Long id) {
        Board board = boardService.findBoardByid(id);
        boardService.countBoard(id);
        model.addAttribute("board", board);
        return "view";
    }

    @GetMapping("/writeform")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "writeform";
    }

    @PostMapping("/writeform")
    public String writeBoard(@ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("message", "Board registered successfully");
        return "redirect:/list";
    }

    @GetMapping("/updateform/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardByid(id);
        model.addAttribute("board", board);
        return "updateform";
    }

    @PostMapping("/updateform")
    public String updateBoard(@RequestParam Long id, @RequestParam String password, @ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        if (boardService.checkPassword(id, password)) {
            boardService.updateBoardByid(board);
            redirectAttributes.addFlashAttribute("message", "업데이트가 성공되었습니다.");
            return "redirect:/list";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/updateform/" + id;
        }
    }

    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "deleteform";
    }

    @PostMapping("/deleteform")
    public String deleteBoard(@RequestParam Long id, @RequestParam String password, RedirectAttributes redirectAttributes){
        if (boardService.checkPassword(id, password)) {
            boardService.deleteBoardByid(id);
            redirectAttributes.addFlashAttribute("message", "Board deleted successfully");
            return "redirect:/list";
        } else {
            redirectAttributes.addFlashAttribute("message", "Board deleted fail");
            return "redirect:/deleteform/" + id;
        }
    }
}