package com.study.basicboard.domain.enum_class;

public enum BoardCategory {
    FREE,GREETING,CLOTHES,FOOD,MOVIE,GOLD,PLACE;

    public static BoardCategory of(String category) {
        if (category.equalsIgnoreCase("free")) return BoardCategory.FREE;
        else if (category.equalsIgnoreCase("greeting")) return BoardCategory.GREETING;
        else if (category.equalsIgnoreCase("gold")) return BoardCategory.GOLD;
        else if (category.equalsIgnoreCase("clothes")) return BoardCategory.CLOTHES;
        else if (category.equalsIgnoreCase("food")) return BoardCategory.FOOD;
        else if (category.equalsIgnoreCase("movie")) return BoardCategory.MOVIE;
        else if (category.equalsIgnoreCase("place")) return BoardCategory.PLACE;
        else return null;
    }
}
