package com.com.urise.webapp.exception;

/**
 * этот эксепшн будем выбрасывать когда объект не найден(для update())
 */
public class NotExistStorageException extends StorageException {

    /**
     * супер значит, что создание объекта этого класса делегируется родителю, те в родительский конструктор
     * посылаем сообщение и проблемный uuid.
     */

    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " not exist", uuid);

        /**
         * super здесь должно идти первой строчкой-тк сначала конструируется базовый класс а потом наш объект
         * также здесь можно дописать дополнительную логику, но вызывать отсюда другие методы не стоит
         * тк объект будет еще не до конца сформирован.
         */
    }
}
