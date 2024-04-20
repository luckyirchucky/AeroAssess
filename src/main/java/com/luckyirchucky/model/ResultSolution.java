package com.luckyirchucky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Конечный результат вычислений
 * Содержит набор исходных данных и решений
 */
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultSolution {
    private String EKFInputParamsFromFile;
}
