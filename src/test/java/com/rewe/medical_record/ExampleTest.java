package com.rewe.medical_record;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExampleTest {
//    @Spy
//    List<String> list = new ArrayList<>();
    @Test
    void example() {
        ArrayList<String> list = spy(ArrayList.class);
        list.add("one");
        list.add("two");
        assertThat(list.size()).isEqualTo(2);
    }
}
