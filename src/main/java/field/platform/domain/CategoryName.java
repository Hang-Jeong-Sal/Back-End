package field.platform.domain;

public enum CategoryName {
    자투리텃밭, 주말농장, 옥상정원, 스쿨팜, 베란다텃밭;

    public static CategoryName of(String name) {
        switch (name) {
            case "자투리텃밭":
                return 자투리텃밭;
            case "주말농장":
                return 주말농장;
            case "옥상정원":
                return 옥상정원;
            case "스쿨팜":
                return 스쿨팜;
            case "베란다텃밭":
                return 베란다텃밭;
        }
        return null;
    }
}
