package com.aisdlc.coding.model;

import java.util.List;

public record CodeGenerationResult(

        List<CodeChange> changes

) {
}