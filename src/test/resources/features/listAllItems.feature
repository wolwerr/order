Feature: List all items

  Scenario: Retrieve all items
    Given the system has the following items
      | uuid                                 | name       | quantity | value | createdAt          | updatedAt          |
      | 123e4567-e89b-12d3-a456-426614174000 | Item 1     | 10       | 100   | 2023-01-01T00:00:00 | 2023-01-01T00:00:00 |
      | 123e4567-e89b-12d3-a456-426614174001 | Item 2     | 20       | 200   | 2023-01-02T00:00:00 | 2023-01-02T00:00:00 |
    When I request the list of items
    Then the response should contain the following items
      | uuid                                 | name       | quantity | value | createdAt          | updatedAt          |
      | 123e4567-e89b-12d3-a456-426614174000 | Item 1     | 10       | 100   | 2023-01-01T00:00:00 | 2023-01-01T00:00:00 |
      | 123e4567-e89b-12d3-a456-426614174001 | Item 2     | 20       | 200   | 2023-01-02T00:00:00 | 2023-01-02T00:00:00 |