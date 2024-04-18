en V4 de springBatch , le composant utilitaire est censé être associé à un seul job
DES SON INITIALISATION
-------------------------
il faut donc retoucher les classes de tests avec des @ContextConfiguration(classes = UniqueJobConfig.class)
....