package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 페이징 게시물 조회
    public Page<Board> findAllBoards(Pageable pageable){
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findAll(sortedByDescId);
    }

    // 상세조회
    @Transactional(readOnly = true)
    public Board findBoardByid(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    // 등록 및 수정
    @Transactional
    public Board saveBoard(Board board){
        return boardRepository.save(board);
    }


    // 삭제
    @Transactional
    public void deleteBoardByid(Long id){
        boardRepository.deleteById(id);
    }

    public boolean checkPassword(Long id, String password) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            return board.getPassword().equals(password);
        }
        return false;
    }



}
