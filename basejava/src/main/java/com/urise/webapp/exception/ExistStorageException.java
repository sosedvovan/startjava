package com.urise.webapp.exception;

/**
 * //
 * этот эксепшн будем выбрасывать когда объект уже существует(для save()).
 */
public class ExistStorageException extends StorageException {

    /**
     * конструктор:
     */
    public ExistStorageException(String uuid) {

        /**
         * супер значит, что создание объекта этого класса делегируется родителю, те в родительский конструктор
         * посылаем сообщение и проблемный uuid.
         */
        super("Resume " + uuid + " already exist", uuid);

        /**
         * super здесь должно идти первой строчкой-тк сначала конструируется базовый класс а потом наш объект
         * также здесь можно дописать дополнительную логику, но вызывать отсюда другие методы не стоит
         * тк объект будет еще не до конца сформирован.
         */
    }


}
