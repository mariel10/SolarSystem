class Matrix3 {
    double[] values;
    Matrix3(double[] values) {
        this.values = values;
    }
    Matrix3(){}

    //перемножение матриц 4 на 4
    Matrix3 multiply4(Matrix3 other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix3(result);
    }

    //умножение матрицы на вектор
    Vertex transform4(Vertex in) {
        return new Vertex(in.x * values[0] + in.y * values[1] + in.z * values[2] + values[3] ,
                in.x * values[4] + in.y * values[5] + in.z * values[6] + values[7],
                in.x * values[8] + in.y * values[9] + in.z * values[10] + values[11]);
    }
    //создание мартицы увеличения размера
    Matrix3 getScaleMatrix(double scale){
        return new Matrix3(new double[]{
                    scale, 0, 0, 0,
                    0, scale, 0, 0,
                    0, 0, scale, 0,
                    0, 0, 0, 1});
    }
    //создание матрицы перемещения небесного тела
    Matrix3 getMoveMatrix(double[] xyz){
        return new Matrix3(new double[]{
                1, 0, 0, xyz[0],
                0, 1, 0, xyz[1],
                0, 0, 1, xyz[2],
                0, 0, 0, 1
        });
    }

}