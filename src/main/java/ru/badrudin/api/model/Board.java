package ru.badrudin.api.model;

public class Board {
    public Long id;
    public Long boardGroupId;
    public String boardId;
    public String description;
    public Boolean isTraded;

    public Board(Long id, Long boardGroupId, String boardId, String description, Boolean isTraded) {
        this.id = id;
        this.boardGroupId = boardGroupId;
        this.boardId = boardId;
        this.description = description;
        this.isTraded = isTraded;
    }

    public String toString() {
        return "Board:{Id: " + id + ", BoardGroupId:" + boardGroupId + ", BoardId:" + boardId + ", Description:" + description + ", IsTraded:" + isTraded + "}";
    }
}
