Feature: WebConfig CORS configuration

  Scenario: Deve adicionar CORS mappings corretamente
    Given que o WebConfig está configurado
    When o CORS mappings é adicionado
    Then deve adicionar o mapping "/**" com origins "http://localhost:8000"
    And deve permitir métodos "GET", "POST", "PUT", "DELETE", "OPTIONS"
    And deve permitir headers "*"
    And deve permitir credenciais
