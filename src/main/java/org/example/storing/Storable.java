package org.example.storing;

import java.util.Scanner;

public interface Storable {

    String serialize();
    void deserialize(Scanner scanner);

}
