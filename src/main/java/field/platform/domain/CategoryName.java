package field.platform.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryName {
    spare("spare"), weekly("weekly"), rooftop("rooftop"), school("school"), terrace("terrace");
    private final String key;

    public static CategoryName of(String string) {
        switch (string) {
            case "spare" :
                return spare;
            case "weekly":
                return weekly;
            case "rooftop":
                return rooftop;
            case "school":
                return school;
            case "terrace":
                return terrace;
            default:
                return null;
        }
    }


}
